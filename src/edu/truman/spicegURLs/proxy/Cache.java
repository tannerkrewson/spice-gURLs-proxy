package edu.truman.spicegURLs.proxy;
import java.util.HashMap;
import java.io.*;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.lang.Runtime;
public class Cache {

	HashMap<String, CacheItem> CacheStore = null;
	public Cache(){
		try{
			FileInputStream fis = new FileInputStream("hashcache.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			CacheStore = (HashMap<String,CacheItem>) ois.readObject();
			ois.close();
			fis.close();
			Set<String> startUpList = CacheStore.keySet();
			Iterator<String> itrSet = startUpList.iterator();
			while(itrSet.hasNext()){
				CacheStore.get(itrSet.next()).startTimer();
			}
		}catch(Exception e){
			System.err.println("Cache either does not exist or could not be processed\n creating a new Cache");
			CacheStore = new HashMap<String,CacheItem>();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				shutDown();
			}
			
		}));
	}
	public void addItem(CacheItem in){
		if(!this.CacheStore.containsKey(in.getRequestURL())){
			this.CacheStore.put(in.getRequestURL(),in);
		}
		else{
			this.CacheStore.replace(in.getRequestURL(),in);
		}
	}
	public void removeItem(String key){
		CacheStore.remove(key);
	}
	public CacheItem getItem(String key){
		return CacheStore.get(key);
	}
	public void shutDown(){
		try{
			FileOutputStream foe = new FileOutputStream("hashcache.ser", false);
			ObjectOutputStream oos = new ObjectOutputStream(foe);
			oos.writeObject(CacheStore);
			oos.close();
			foe.close();
		}catch(Exception e){
			System.err.println("Could not write Cache");
			System.err.println(e);
		}
	}
}
