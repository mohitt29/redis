package com.mycache.store;

import java.util.*;
import java.util.concurrent.*;

/**
* This class represents the core data structures we will use to store data 
*/
public class InMemoryStore {

	public InMemoryStore() {
		startCacheCleanerThread();
	}

	private final Map<String, Data> store = new ConcurrentHashMap<>();
	
	public String get(String key) {
		if(!store.containsKey(key)) {
			return null;
		}
		if(!isCacheEntryValid(key)) {
			store.remove(key);
			return null;
		}
		Data data = store.get(key);
		
		return data.getData();
	}

	public void set(String key, String value) {
		Data data = new Data(key, value);
		store.put(key, data);
	}
	
	public void set(String key, String value, long ttl) {
		Data data = new Data(key, value, ttl);
		store.put(key, data);
	}

	public int size() {
		invalidateCache();
		return store.size();
	}

	public String delete(String key) {
		return store.remove(key).getData();
	}

	public Map<String, Data> getSnapshot() {
		return new HashMap<>(store);
	}

	private void startCacheCleanerThread() {
		new Thread(() -> {
			try {
				while(true) {
					invalidateCache();
					Thread.sleep(5000);
				}
			}
			catch(Exception e) {
				System.out.println("Exception occurred while cleaning cache");
			}
		}).start();
	}

	private void invalidateCache() {
		Set<String> expiredKeys = new HashSet<>();
		for(Map.Entry<String, Data> entry: store.entrySet()) {
			String key = entry.getKey();
			if(!isCacheEntryValid(key)) {
				expiredKeys.add(key);
			}
		}
		store.keySet().removeAll(expiredKeys);
	}

	private boolean isCacheEntryValid(String key) {
		Data data = store.get(key);
		if(data.getExpirationTime() != -1 && data.getExpirationTime() < System.currentTimeMillis()) {	
			return false;
		}
		return true;
	}

}

