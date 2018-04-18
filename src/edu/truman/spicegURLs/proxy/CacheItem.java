package edu.truman.spicegURLs.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.Timer;

public class CacheItem {
	public static final int MS_PER_MIN = 60000;
	public static final int DELAY = 1 * MS_PER_MIN; //43200 min in a month
	private String request;
	private Hashtable<String, CacheItem> cacheList;
	private Timer time;
	
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

	public boolean isSameRequest(String request) {
		if (this.request == request.toLowerCase()) {
			return true;
		}
		return false;
	}
	
	public void resetTimer() {
		time.restart();
	}
	
	private void terminateCacheItem() {
		time.stop();
		cacheList.remove(request);
	}
}
