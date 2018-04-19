package edu.truman.spicegURLs.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;
import javax.swing.Timer;

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
	
	public static final int MS_PER_MIN = 60000;
	public static final int DELAY = MS_PER_MIN / 2;
	private URL request;
	private String page;
	private Date lastModified;
	private Timer time;
	
	/**
	 * Creates the object from a request.
	 * @param request the page being cached
	 */
	public CacheItem(URL request, String page) {
		this.request = request.toLowerCase();
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
	
	public void setRequest(String request) {
		this.request = request;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
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
	
	public String getRequestURL() {
		return request.toString();
	}
}
