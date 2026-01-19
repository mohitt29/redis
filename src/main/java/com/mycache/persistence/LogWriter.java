package com.mycache.persistence;

import java.util.*;
import java.io.*;

/**
* This class is used to write the commands to a log file, which can be used to restore the cache
*/
public class LogWriter {
	private static final String LOG_FILE_PATH = "data/data.log";

	public void append(String command) {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
			bw.write(command);
			bw.newLine();
		}
		catch(Exception e) {
			System.out.println("Unable to append to log file");
			e.printStackTrace();
		}
	}

}
