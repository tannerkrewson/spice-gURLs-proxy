package edu.truman.spicegURLs.proxy;

import java.io.*;
import java.net.*;

/**
 * An object which acts as a client and processes requests.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 21 April 2018
 */
public class ProxySession implements Runnable {
	
	private Socket client;
	private Cache cache;
	
	/**
	 * Creates an instance of a ProxySession
	 * @param client the machine connected to this proxy
	 * @param cache a reference to the global cache
	 */
	public ProxySession(Socket client, Cache cache) {
		this.client = client;
		this.cache = cache;
	}
	
	/**
	 * Looks into the connection and returns the HTTP header
	 * as a String array, split on spaces.
	 * @return string array of the parts of the header
	 * @throws Exception
	 */
	private String[] getHeaderOfRequest() throws IOException {
		BufferedReader inStream
			= new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		// parse the first line of the request that looks like this:
		// GET /proxy/http://google.com HTTP/1.1
		String requestHeader = inStream.readLine();
		
		if (requestHeader == null || !requestHeader.startsWith("GET")) {
			throw new IOException("501 Not Implemented");
		}
			
		return requestHeader.split(" ");
	}
	
	/**
	 * Gets and validates the URL out of an HTTP request
	 * @param req an HTTP request header, split on spaces
	 * @return the URL from the request
	 * @throws FileNotFoundException request doesn't start with /proxy/
	 * @throws IOException URL from request is malformed
	 */
	private URL getURLFromRequest(String[] req) throws IOException {
		
		String url = req[1];
		
		if (url.startsWith("/")) {
			url = url.substring(1);
		}
		
		// make sure it has http://
		if (!url.toLowerCase().matches("^\\w+://.*")) {
		    url = "http://" + url;
		}
		
		if(!url.toLowerCase().matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
			throw new IOException("400 Bad Request");
		}
		
		return new URL(url);
	}
	
	/**
	 * Send a response to our client with the given code and no further
	 * content.
	 * @param code HTTP response code
	 * @throws Exception
	 */
	private void sendResponse(String code) throws Exception {
		sendResponse(new HttpResponse(code));
	}
	
	/**
	 * Send a response to our client with the given code and given response 
	 * attached.
	 * @param code HTTP response code
	 * @param response HTML or things to send back
	 * @throws Exception
	 */
	private void sendResponse(HttpResponse response) throws Exception {
		client.getOutputStream().write(response.getRawResponse());
		client.close();
	}
		
	/**
	 * Parses the client's request, requests it, and sends the response 
	 * back to them.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			String[] requestHeader = getHeaderOfRequest();
			
			URL urlToGet = getURLFromRequest(requestHeader);
			
			CacheItem ci = cache.getItem(urlToGet.toString());
			if (ci == null) {
				ci = new CacheItem(urlToGet);
				ci.requestInitialPage();
				cache.addItem(ci);
			}
			HttpResponse page = ci.getPage();
			sendResponse(page);
	     
		} catch (IOException e) {
			try {
				System.err.println(e.getMessage());
				sendResponse(e.getMessage());
			} catch (Exception e2) {
				e2.printStackTrace(System.out);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 
	}

}
