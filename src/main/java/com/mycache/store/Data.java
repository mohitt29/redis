package com.mycache.store;

import java.util.*;
import java.time.*;

public class Data {
	private String key;
	private String data;
	private long creationTime;
	private long expirationTime = -1;
	private long ttl;

	public Data(String key, String data) {
		this.key = key;
		this.data = data;
		this.creationTime = System.currentTimeMillis();
	}

	public Data(String key, String data, long ttl) {
		this.key = key;
		this.data = data;
		this.ttl = ttl;
		this.creationTime = System.currentTimeMillis();
		this.expirationTime = creationTime + ttl;
	}


	public String getData() {
		return data;
	}

	public long getExpirationTime() {
		return expirationTime;
	}
	
	public String toString() {
		return String.format("[Key: %s, Value: %s, Expiration Time: %d, Current Time: %d]", key, data, expirationTime, System.currentTimeMillis());
	}

}


