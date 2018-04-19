package edu.truman.spicegURLs.proxy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
	
	public ProxySession(Socket client, Cache cache) {
		this.client = client;
		this.cache = cache;
	}
	
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
	
	// TODO: We can also check for 304 in this method
	private URL getURLFromRequest(String[] req) throws FileNotFoundException, IOException {	
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
	
	private void sendResponse(String code) throws Exception {
		sendResponse(code, "");
	}
	
	private void sendResponse(String code, String response) throws Exception {
        String httpResponse = "HTTP/1.1 " + code + "\r\n\r\n" + response;
    	client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        client.close();
	}
	
	@Override
	public void run() {
		try {
			String[] requestHeader = getHeaderOfRequest();
			
			URL urlToGet = getURLFromRequest(requestHeader);
			
			// This is where we'll check for 304
			String response;
			
			/*CacheItem ci = cache.get(urlToGet);
			if (ci != null) {
				response = getResponseFromURL(urlToGet, ci.getLastModified());
				ci.setPage(response);
			} else {*/
				response = getResponseFromURL(urlToGet, new Date());
				cache.addItem(new CacheItem(urlToGet, response));
			//}
			
			sendResponse("200 OK", response);
	     
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
