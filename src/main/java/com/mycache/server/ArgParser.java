package com.mycache.server;

import java.util.*;

public class ArgParser {
	public static ServerConfig parseArgs(String[] args) {
		ServerConfig config = new ServerConfig();

		for(int i = 0; i < args.length; i++) {
			String current = args[i];

			switch (current) {
				case "--port":
					config.port = Integer.parseInt(args[++i]);
					break;

				case "--role":
					config.role = NodeRole.valueOf(args[++i].toUpperCase());
					break;

				case "--restore":
					config.restore = true;
					break;

				case "--replica-of":
					String[] hostPort = args[++i].split(":");
					config.masterHost = hostPort[0];
					config.masterPort = Integer.parseInt(hostPort[1]);
					break;

				default:
					throw new IllegalArgumentException("Argument not supported: " + current);
			}
		}
		return config;
	}
}
