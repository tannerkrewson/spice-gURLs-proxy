package edu.truman.spicegURLs.proxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Scanner;
public class ProxySession implements Runnable {
	
	private Socket client;
	
	public ProxySession(Socket client){
		this.client = client;
	}
	
	private String[] getHeaderOfRequest() throws Exception {
		BufferedReader inStream
			= new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		// parse the first line of the request that looks like this:
		// GET /proxy/http://google.com HTTP/1.1
		String requestHeader = inStream.readLine();
		
		if (!requestHeader.startsWith("GET")) {
			throw new Exception("Bad request: " + requestHeader);
		}
				
		return requestHeader.split(" ");
	}
	
	private URL getURLFromRequest(String[] req) throws IOException {	
		if (!req[1].startsWith("/proxy/")) {
			throw new IOException("Ignored: " + req[1]);
		}
		
		String url = req[1].substring(7);
		
		if (url.length() == 0) {
			throw new IOException();
		}
		
		// browsers will ask for this with every page
		if (url.equals("favicon.ico")) {
			throw new IOException();
		}
		
		// make sure it has http://
		if (!url.toLowerCase().matches("^\\w+://.*")) {
		    url = "http://" + url;
		}
		
		return new URL(url);
	}
	
	private String getResponseFromURL (URL url) throws IOException {
		InputStream response = url.openStream();
		Scanner scanner = new Scanner(response);
	    String responseBody = scanner.useDelimiter("\\A").next();
	    scanner.close();
	    return responseBody;
	}
	
	@Override
	public void run() {
		try {
			String[] requestHeader = getHeaderOfRequest();
			
			URL urlToGet = getURLFromRequest(requestHeader);
			
			String response = getResponseFromURL(urlToGet);
			
	        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + response;
	        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
	        System.out.println("Received " + urlToGet + " from " + client.getInetAddress());
	        client.close();
	        
		} catch (IOException e) {
			System.err.println("HTTP/1.1 404 NOT FOUND \r\n\r\n" + e);
			String httpResponse = "HTTP/1.1 404 Something Crazy \r\n\r\n";
	        try {
	        		System.out.println("Trying to send 404 to client");
				client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				client.close();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println(e);
			//e.printStackTrace(System.out);
		} 
	}

}
