package com.mycache.command;

import java.util.*;
import com.mycache.store.*;

/**
* This class is used for handling different commands
*/
public class CommandHandler {
	
	private static final int PAGE_LIMIT = 5;
	
	public static String handleCommand(String input, InMemoryStore store) {
		String[] parts = input.trim().split(" ");

		String command = parts[0].toUpperCase();
		switch(command) {
			case "SET": {
				if(parts.length != 3 && parts.length != 4) {
					return "ERROR: Usage SET key value [time(s)]";
				}
				String key = parts[1];
				String value = parts[2];

				if(parts.length == 3) {
					store.set(key, value);
				}
				else {
					store.set(key, value, 1000 * Integer.valueOf(parts[3]));
				}
				return "OK";
			}

			case "GET": {
				if(parts.length < 2) {
					return "ERROR: Usage GET key";
				}
				String key = parts[1];
				return store.get(key) == null ? "NULL" : store.get(key);
			}

			case "SIZE": {
				return String.valueOf(store.size());
			}

			case "DEL": {
				if(parts.length < 2) {
					return "ERROR: Usage DEL key";
				}
				String key = parts[1];
				if(store.get(key) == "NULL") {
					return String.format("ERROR: %s is not valid key");
				}
				store.delete(key);
				return String.format("OK: Removed %s", key);
			}

			case "SCAN": {
				if(parts.length != 2 || !parts[1].matches("^\\d+$")) {
					return "ERROR: Usage SCAN [number]";
				}
				List<Map.Entry<String, Data>> dataList = new ArrayList<>(store.getSnapshot().entrySet());

				int start = Integer.parseInt(parts[1]);
				int end = Math.min(start + PAGE_LIMIT, dataList.size());


				StringBuilder answer = new StringBuilder();
				answer.append("NEXT " + end + "\n");
				for(int i = start; i < end; i++) {
					answer.append(dataList.get(i).getKey() + "=" + dataList.get(i).getValue() + "\n");
				}
				return answer.toString();
			}

			default: {
				return String.format("ERROR: %s is not supported", command);
			}
		}
	}
}
