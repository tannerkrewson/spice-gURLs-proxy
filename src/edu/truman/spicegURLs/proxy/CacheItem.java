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

	public URL getRequest() {
		return request;
	}
	
	public String getPage() {
		return page;
	}
	
	public Date getLastModified() {
		return lastModified;
	}

	public String getRequestURL() {
		return request.toString();
	}
	
	public void setRequest(URL request) {
		this.request = request;
	}
	
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
	
	private void updateLastModified() {
		lastModified = new Date();
	}
}
