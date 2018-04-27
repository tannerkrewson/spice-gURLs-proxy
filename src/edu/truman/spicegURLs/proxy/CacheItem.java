package edu.truman.spicegURLs.proxy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import javax.swing.Timer;
import java.awt.event.*;

/**
 * An object which keeps track of cache information and automatically
 * updates after a given period of time.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 22 April 2017
 */
public class CacheItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private URL request;
	private HttpResponse page;
	private Date lastModified;
	private Timer updater;
	Cache cache;
	/**
	 * Creates the object from a request.
	 * @param request the page being cached
	 */
	public CacheItem(URL request, Cache cache) {
		this.request = request;
		this.lastModified = new Date();
		this.cache = cache;
		initTimer();
	}
	
	/**
	 * Defines and initializes update timer for Cache Item object
	 */
	public void initTimer(){
		this.updater = new Timer(30*1000, new ActionListener (){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cache.removeItem(request.toString());
				System.out.println(request.toString() +  " has been deleted from cache");
			}
			
		});
		
		updater.start();
	}
	
	/**
	 * Start the timer for cache item expiration
	 */
	public void restartTimer(){
		updater.restart();
		requestUpdatedPage();
	}

	/**
	 * Gets request URL 
	 * @return request
	 */
	public URL getRequest() {
		return request;
	}
	
	/**
	 * Gets page String
	 * @return page
	 */
	public HttpResponse getPage() {
		return page;
	}
	
	/**
	 * Gets lastModified Date
	 * @return lastModified
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * Gets request String
	 * @return request
	 */
	public String getRequestURL() {
		return request.toString();
	}
	
	/**
	 * Sets request URL
	 * @param request the request to set
	 */
	public void setRequest(URL request) {
		this.request = request;
	}
	
	/**
	 * Sets page String and updates lastModified
	 * @param page the page to set
	 */
	public void setPage(HttpResponse page) {
		this.page = page;
		updateLastModified();
	}
	
	
	/**
	 * Compares the given request with this object's request
	 * and returns whether or not they are equivalent.
	 * @param request the request to compare
	 * @return the comparison
	 */
	public boolean isSameRequest(URL request) {
		if (this.request == request) {
			return true;
		}
		return false;
	}
	
	/**
	 * Requests the web page from the web server without
	 * asking for a last-modified date and time. 
	 * It then caches this response.  
	 */
	public void requestInitialPage () {
		HttpResponse response = getResponseFromURL(this.request, null);
		updateCache(response);
	}
	
	/**
	 * Requests the web page from the web server using the 
	 * if-modified-since date. It then caches this response 
	 * if the web server responds with a page more recent than
	 * the one located in the cache. 
	 */
	public void requestUpdatedPage () {
		HttpResponse response = 
				getResponseFromURL(this.request, this.lastModified);
		updateCache(response);
	}
	
	/**
	 * Places the response from the web server into the cache
	 * if the web page has been modified.
	 * @param response the web page delivered from the web server to this proxy
	 */
	private void updateCache(HttpResponse response){
		String resCode = response.getResponseCode();
		
		if (!resCode.startsWith("304")) {
			this.updateLastModified();
			this.setPage(response);
		}
	}
	
	/**
	 * Resets the value of lastModified to the current time.
	 */
	private void updateLastModified() {
		lastModified = new Date();
	}
	
	/**
	 * Makes a request to the given URL with the given if-modified-since
	 * time, and returns the contents of the result as a string, usually 
	 * raw HTML.
	 * @param url the URL to send a GET request to
	 * @param ims the if-modified-since time
	 * @return the result of the request
	 */
	private HttpResponse getResponseFromURL (URL url, Date ims) {
		try {
			// sends a request to the given url
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			if (ims != null) {
				con.setIfModifiedSince(ims.getTime());
			}
			con.connect();
			
			// parses and prints the response status to the console
			String resStatus = 
					con.getResponseCode() + " " + con.getResponseMessage();
			System.out.println(resStatus + ": " + url);
			
			// if the response was not okay, jump to the catch
			if (con.getResponseCode() != 200) {
				throw new IOException(resStatus);
			}
			
			// read in the response as a byte array
			InputStream is = con.getInputStream();
			ByteArrayOutputStream responseData = new ByteArrayOutputStream();
			int nRead;
			byte[] buffer = new byte[16384];
			while ((nRead = is.read(buffer, 0, buffer.length)) != -1) {
				responseData.write(buffer, 0, nRead);
			}
			responseData.flush();

			// construct an http response and return it
			return new HttpResponse(
					resStatus, 
					con.getHeaderFields(),
					responseData.toByteArray()
			);
			
		} catch (IOException e) {
			// constructs an http response with the error code, 
			// and does not include any data, because error responses
			// won't have any data.
			return new HttpResponse(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
