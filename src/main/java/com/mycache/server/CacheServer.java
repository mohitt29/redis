package com.mycache.server;

import java.util.*;
import java.io.*;
import java.net.*;
import com.mycache.store.*;
import com.mycache.command.*;

/**
* This class contains the actual server code, which accepts incoming client connections
*/
public class CacheServer {
	private static final int PORT = 6379;
	private static final InMemoryStore cache = new InMemoryStore();
	
	public static void main(String[] args) throws Exception {
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
				out.println(CommandHandler.handleCommand(line, cache));
			}
		}
		catch(IOException e) {
			System.out.println("Error occurred while handling input from client");
			e.printStackTrace();
		}
	}
}

