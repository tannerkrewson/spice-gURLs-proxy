package edu.truman.spicegURLs.proxy;

import java.net.URL;
import java.util.Date;

/**
 * An object which keeps track of cache information and automatically
 * self-terminates after a given period of time.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 17 April 2017
 */
public class CacheItem {
	
	private URL request;
	private String page;
	private Date lastModified;
	
	/**
	 * Creates the object from a request.
	 * @param request the page being cached
	 */
	public CacheItem(URL request, String page) {
		this.request = request;
		this.page = page;
		this.lastModified = new Date();
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
	public String getPage() {
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
	public void setPage(String page) {
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
	 * Resets the value of lastModified to the current time.
	 */
	private void updateLastModified() {
		lastModified = new Date();
	}
}
