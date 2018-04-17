package edu.truman.spicegURLs.proxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
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
	
	@Override
	public void run() {
		try {
			URL urlToGet = getURLFromRequest();
			
			String message = "spice gURLs reporting for duty!\n";
			message += urlToGet.toString();
			
	        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + message;
	        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
	        
	        System.out.println("Received " + urlToGet + " from " + client.getInetAddress());
	        
	        client.close();
		} catch(Exception e) {
			System.err.println(e);
		}
	}

}
