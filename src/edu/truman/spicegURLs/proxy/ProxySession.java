package edu.truman.spicegURLs.proxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
public class ProxySession implements Runnable {
	
	private Socket client;
	
	public ProxySession(Socket client){
		this.client = client;
	}
	
	private URL getURLFromRequest() throws IOException{
		BufferedReader inStream
			= new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		// parse the first line of the request that looks like this:
		// GET /http://google.com HTTP/1.1
		String requestHeader = inStream.readLine();
				
		// grab the url part only
		String url = requestHeader.substring(5);
		url = url.substring(0, url.length()-9);
		
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
			URL urlToGet = getURLFromRequest();
			
			String response = getResponseFromURL(urlToGet);
			
			String message = "";
			message += response;
			
	        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + message;
	        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
	        
	        System.out.println("Received " + urlToGet + " from " + client.getInetAddress());
	        
	        client.close();
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}

}
