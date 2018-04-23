package edu.truman.spicegURLs.proxy;

import java.util.HashMap;
import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.lang.Runtime;

/**
 * An object which stores all of the CacheItems for the Proxy and
 * provides accessors and mutators for the objects it holds.
 * Additionally it saves the cached information on proper termination.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 22 April 2017
 */
public class Cache {

	HashMap<String, CacheItem> CacheStore = null;
	
	/**
	 * Creates a Cache object which checks for a stored file
	 * and loads it if there is one. Otherwise it creates one.
	 */
	public Cache () {
		try {
			FileInputStream fis = new FileInputStream("hashcache.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			CacheStore = (HashMap<String,CacheItem>) ois.readObject();
			ois.close();
			fis.close();
			Set<String> startUpList = CacheStore.keySet();
			Iterator<String> itrSet = startUpList.iterator();
			while (itrSet.hasNext()) {
				String key = itrSet.next();
				CacheStore.get(key).initTimer();
				CacheStore.get(key).requestUpdatedPage();
			}
		} catch (Exception e) {
			String errMess = "Cache either does not exist or could not";
			errMess += "be processed\ncreating a new Cache";
			System.err.println(errMess);
			CacheStore = new HashMap<String,CacheItem>();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run () {
				shutDown();
			}	
		}));
	}
	
	/**
	 * Adds a CacheItem to the CacheStore
	 * @param in the CacheItem to store
	 */
	public void addItem (CacheItem in) {
		if (!this.CacheStore.containsKey(in.getRequestURL())) {
			this.CacheStore.put(in.getRequestURL(),in);
		}
		else {
			this.CacheStore.replace(in.getRequestURL(),in);
		}
	}
	
	/**
	 * Removes a CacheItem from the CacheStore.
	 * @param key the key of the CacheItem to remove
	 */
	public void removeItem (String key) {
		CacheStore.remove(key);
	}
	
	/**
	 * Returns the CacheItem corresponding to the given key.
	 * Returns NULL if the item is not in the Cache.
	 * @param key the key of the CacheItem to return
	 * @return
	 */
	public CacheItem getItem (String key) {
		return CacheStore.get(key);
	}
	
	/**
	 * Saves the Cache upon termination.
	 */
	public void shutDown () {
		try {
			FileOutputStream foe = new FileOutputStream("hashcache.ser",
					false);
			ObjectOutputStream oos = new ObjectOutputStream(foe);
			oos.writeObject(CacheStore);
			oos.close();
			foe.close();
		} catch (Exception e) {
			System.err.println("Could not write Cache");
			System.err.println(e);
		}
	}
}
