package com.mycache.server;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import com.mycache.store.*;
import com.mycache.command.*;
import com.mycache.persistence.*;

/**
* This class contains the actual server code, which accepts incoming client connections
*/
public class CacheServer {
	private static final InMemoryStore cache = new InMemoryStore();
	private static final LogReader logReader = new LogReader(cache);
	private static ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void main(String[] args) throws Exception {
		ServerConfig config = ArgParser.parseArgs(args);
		startServer(config);
	}

	private static void startServer(ServerConfig config) throws Exception {	
		if(config.role == NodeRole.MASTER) {
			startServerAsMaster(config);
		}
		else {
			startServerAsReplica(config);
		}
	}

	private static void startServerAsMaster(ServerConfig config) throws Exception {
		if(config.restore) {
			logReader.createMap();
		}
		ServerSocket server = new ServerSocket(config.port);
		System.out.println("Server (MASTER) started on port: " + config.port);
		acceptConnections(server);
	}

	private static void startServerAsReplica(ServerConfig config) throws Exception {
		ReplicaNode replica = new ReplicaNode(cache, config.masterHost, config.masterPort);
		replica.startSync();
		System.out.println("Server (REPLICA) starting on port: " + config.port);
	}
	
	private static void acceptConnections(ServerSocket server) throws Exception {
		while(true) {
			Socket client = server.accept();
			pool.submit(() -> handleClient(client));
		}
	}

	private static void handleClient(Socket client) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

			String line;
			while((line = in.readLine()) != null) {
				if("SYNC".equals(line)) {
					handleSync(client);
					return;
				}
				out.println(CommandHandler.handleCommand(line, cache, true));
			}
		}
		catch(Exception e) {
			System.out.println("Error occurred while handling input from client");
			e.printStackTrace();
		}
	}

	private static void handleSync(Socket replica) throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(replica.getOutputStream())), true);
		
		out.println("BEGIN_SYNC");
		for(Map.Entry<String, Data> entry: cache.getSnapshot().entrySet()) {
			String command = "SET " + entry.getKey() + " " + entry.getValue();
			out.println(command);
		}

		out.println("END_SYNC");
	}

}

