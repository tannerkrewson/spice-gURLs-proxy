package edu.truman.spicegURLs.proxy;
import java.util.HashMap;
import java.io.*;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
public class Cache {

	HashMap<String, CacheItem> CacheStore = null;
	public Cache(){
		try{
			FileInputStream fis = new FileInputStream("hashcache.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			CacheStore = (HashMap<String,CacheItem>) ois.readObject();
			ois.close();
			fis.close();
		}catch(Exception e){
			System.err.println("Cache either does not exist or could not be processed\n creating a new Cache");
			CacheStore = new HashMap<String,CacheItem>();
		}
	}
	public void addItem(CacheItem in){
		this.CacheStore.put(in.getRequestURL(),in);
	}
	public void removeItem(){
		
	}
	public void shutDown(){
		try{
			FileOutputStream foe = new FileOutputStream("hashcache.ser");
			ObjectOutputStream oos = new ObjectOutputStream(foe);
			oos.writeObject(CacheStore);
			oos.close();
			foe.close();
		}catch(Exception e){
			System.err.println("Could not write Cache");
		}
	}
}
