package com.mycache.persistence;

import java.util.*;
import java.io.*;
import com.mycache.store.*;
import com.mycache.command.*;

/**
* This class is used to read logs from a valid log file and update it to map
*/
public class LogReader {
	private static final String LOG_FILE_PATH = "data/data.log";
	private InMemoryStore store = null;

	public LogReader(InMemoryStore store) {
		this.store = store;
	}

	public void createMap() {
		File file = new File(LOG_FILE_PATH);
		if(!file.exists()) {
			System.out.println("Data file not found, starting fresh server");
			return;	
		}

		System.out.println("Creating cache from data/data.log file");
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line = br.readLine()) != null) {
				CommandHandler.handleCommand(line, store, false);
			}
		}
		catch(Exception e) {
			System.out.println("Unable to process data file");
			e.printStackTrace();
		}
	}

}
