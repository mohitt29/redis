package com.mycache.server;

import java.util.*;
import java.io.*;
import java.net.*;
import com.mycache.store.*;
import com.mycache.command.*;


public class ReplicaNode {
	private final InMemoryStore store;
	private final String masterHost;
	private final int masterPort;

	public ReplicaNode(InMemoryStore store, String masterHost, int masterPort) {
		this.store = store;
		this.masterHost = masterHost;
		this.masterPort = masterPort;
	}

	public void startSync() throws Exception {
		Socket socket = new Socket(masterHost, masterPort);

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		out.println("SYNC");

		String line;
		while((line = in.readLine()) != null) {
			if("BEGIN_SYNC".equals(line)) {
				continue;
			}
			if("END_SYNC".equals(line)) {
				break;
			}

			CommandHandler.handleCommand(line, store, false);
		}

		new Thread(() -> listenForUpdates(in)).start();
	}

	private void listenForUpdates(BufferedReader in) {
		try {
			String line;
			while((line = in.readLine()) != null) {	
				CommandHandler.handleCommand(line, store, false);
			}
		}
		catch(Exception e) {
			System.err.println("Lost connection to master");
		}
	}
}
