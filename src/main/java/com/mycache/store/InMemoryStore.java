package com.mycache.store;

import java.util.*;
import java.util.concurrent.*;

/**
* This class represents the core data structures we will use to store data 
*/
public class InMemoryStore {
	private final Map<String, String> store = new ConcurrentHashMap<>();
	
	public String get(String key) {
		return store.get(key);
	}

	public void set(String key, String value) {
		store.put(key, value);
	}

	public int size() {
		return store.size();
	}

	public String delete(String key) {
		return store.remove(key);
	}
}
