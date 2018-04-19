package edu.truman.spicegURLs.proxy;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

/**
 * An object which acts as a client and processes requests.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 17 April 2018
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
	private String[] getHeaderOfRequest() throws Exception {
		BufferedReader inStream
			= new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		// parse the first line of the request that looks like this:
		// GET /proxy/http://google.com HTTP/1.1
		String requestHeader = inStream.readLine();
		
		// TODO: This is prolly where we'd do the 501 error
		if (!requestHeader.startsWith("GET")) {
			throw new Exception("Bad request: " + requestHeader);
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
	private URL getURLFromRequest(String[] req) throws FileNotFoundException, IOException {
		// TODO: We can also check for 304 in this method
		
		if (!req[1].startsWith("/proxy/")) {
			throw new FileNotFoundException("Ignored: " + req[1]);
		}
		
		String url = req[1].substring(7);
		
		// TODO: This is where we can check for the 400 malformed error
		if (url.length() == 0) {
			throw new IOException();
		}
		
		// make sure it has http://
		if (!url.toLowerCase().matches("^\\w+://.*")) {
		    url = "http://" + url;
		}
		
		return new URL(url);
	}
	
	/**
	 * Makes a request to the given URL with the given if-modified-since
	 * time, and returns the contents of the result as a string, usually 
	 * raw HTML.
	 * @param url the URL to send a GET request to
	 * @param ims the if-modified-since time
	 * @return the result of the request
	 * @throws Exception
	 */
	private String getResponseFromURL (URL url, Date ims) throws Exception {
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setIfModifiedSince(ims.getTime());
		con.connect();
		
		InputStream response = con.getInputStream();
		Scanner scanner = new Scanner(response);
	    String responseBody = scanner.useDelimiter("\\A").next();
	    scanner.close();
	    return responseBody;
	}
	
	/**
	 * Send a response to our client with the given code and no further
	 * content.
	 * @param code HTTP response code
	 * @throws Exception
	 */
	private void sendResponse(String code) throws Exception {
		sendResponse(code, "");
	}
	
	/**
	 * Send a response to our client with the given code and given response 
	 * attached.
	 * @param code HTTP response code
	 * @param response HTML or things to send back
	 * @throws Exception
	 */
	private void sendResponse(String code, String response) throws Exception {
        String httpResponse = "HTTP/1.1 " + code + "\r\n\r\n" + response;
    	client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
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
			
			// This is where we'll check for 304
			String response;
			
			CacheItem ci = cache.getItem(urlToGet.toString());
			if (ci != null) {
				response = getResponseFromURL(urlToGet, ci.getLastModified());
				ci.setPage(response);
				sendResponse("305 Use Proxy", response);
			} else {
				response = getResponseFromURL(urlToGet, new Date());
				cache.addItem(new CacheItem(urlToGet, response));
				sendResponse("200 OK", response);
			}
	     
		} catch (FileNotFoundException e) {
			try {
				sendResponse("204 No Content");
			} catch (Exception e2) {
				e2.printStackTrace(System.out);
			}
		} catch (IOException e) {
			try {
				sendResponse("400 Bad Request");
			} catch (Exception e2) {
				e2.printStackTrace(System.out);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 
	}

}
