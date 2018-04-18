package edu.truman.spicegURLs.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
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
	public static final int DELAY = 1 * MS_PER_MIN; //43200 min in a month
	private String request;
	private Hashtable<String, CacheItem> cacheList;
	private Timer time;
	
	/**
	 * Creates the object from a request and is given access to the
	 * list in which it is held.
	 * @param request the page being cached
	 * @param cacheList the list in which this object is contained
	 */
	public CacheItem(String request, Hashtable<String, CacheItem> cacheList) {
		this.request = request.toLowerCase();
		this.cacheList = cacheList;
		time = new Timer(DELAY, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				terminateCacheItem();
			}
		});
	}

	/**
	 * Compares the given request with this object's request
	 * and returns whether or not they are equivalent.
	 * @param request the request to compare
	 * @return the comparison
	 */
	public boolean isSameRequest(String request) {
		if (this.request == request.toLowerCase()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Resets the timer
	 */
	public void resetTimer() {
		time.restart();
	}
	
	//ifModifiedSince
	
	//checkForUpdates
	
	/**
	 * Removes this object from the list when it has expired.
	 */
	private void terminateCacheItem() {
		time.stop();
		cacheList.remove(request);
	}
}
