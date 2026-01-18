package com.mycache.server;

import java.util.*;
import java.io.*;
import java.net.*;
import com.mycache.store.*;
import com.mycache.command.*;
import com.mycache.persistence.*;

/**
* This class contains the actual server code, which accepts incoming client connections
*/
public class CacheServer {
	private static final int PORT = 6379;
	private static final InMemoryStore cache = new InMemoryStore();
	private static final LogReader logReader = new LogReader(cache);

	public static void main(String[] args) throws Exception {
		if(args.length > 0 && "--restore".equals(args[0])) {
			logReader.createMap();
		}

		ServerSocket server = new ServerSocket(PORT);
		System.out.println("Server started on port: " + PORT);

		while(true) {
			Socket client = server.accept();
			new Thread(() -> handleClient(client)).start();
		}
	}

	private static void handleClient(Socket client) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

			String line;
			while((line = in.readLine()) != null) {
				out.println(CommandHandler.handleCommand(line, cache, true));
			}
		}
		catch(IOException e) {
			System.out.println("Error occurred while handling input from client");
			e.printStackTrace();
		}
	}
}

