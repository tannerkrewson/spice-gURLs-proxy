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
import javax.swing.Timer;
import java.awt.event.*;

/**
 * An object which keeps track of cache information and automatically
 * self-terminates after a given period of time.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 17 April 2017
 */
public class CacheItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private URL request;
	private HttpResponse page;
	private Date lastModified;
	private Timer updater;
	
	/**
	 * Creates the object from a request.
	 * @param request the page being cached
	 */
	public CacheItem(URL request) {
		this.request = request;
		this.lastModified = new Date();
		initTimer();
	}
	/**
	 * defines and initializes update timer for Cache Item object
	 */
	public void initTimer(){
		this.updater = new Timer(30*1000, new ActionListener (){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				requestUpdatedPage();
				updater.restart();
			}
			
		});
		
		this.startTimer();
	}
	
	/**
	 * Start the timer that checks to see if the page has been updated.
	 */
	private void startTimer(){
		updater.start();
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
	
	public void requestInitialPage () {
		HttpResponse response = getResponseFromURL(this.request, null);
		updateCache(response);
	}
	
	public void requestUpdatedPage () {
		HttpResponse response = getResponseFromURL(this.request, this.lastModified);
		updateCache(response);
	}
	
	private int updateCache(HttpResponse response){
		String resCode = response.getResponseCode();
		
		if (!resCode.startsWith("304")) {
			this.updateLastModified();
			this.setPage(response);
		}
		return 0;
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
	 * @throws Exception
	 */
	private HttpResponse getResponseFromURL (URL url, Date ims) {
		try {
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			if (ims != null) {
				con.setIfModifiedSince(ims.getTime());
			}
			con.connect();
			
			String resStatus = con.getResponseCode() + " " + con.getResponseMessage();
			
			System.out.println(resStatus + ": " + url);
			
			if (con.getResponseCode() != 200) {
				throw new IOException(resStatus);
			}
			
			InputStream is = con.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();

			return new HttpResponse(
					resStatus, 
					con.getHeaderFields(),
					buffer.toByteArray());	
		} catch (IOException e) {
			return new HttpResponse(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
