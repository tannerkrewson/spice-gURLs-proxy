package edu.truman.spicegURLs.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private String request;
	private Date lastUpdated;
	private Timer time;
	
	/**
	 * Creates the object from a request.
	 * @param request the page being cached
	 */
	public CacheItem(String request) {
		this.request = request.toLowerCase();
		lastUpdated = new Date();
		time = new Timer(DELAY, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkForUpdates();
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
	
	public boolean checkIfUpdatedSince(Date check) {
		if (lastUpdated.after(check)) {
			return true;
		}
		return false;
	}
	
	private void checkForUpdates() {
		time.restart();
	}
}
