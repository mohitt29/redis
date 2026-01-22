package com.mycache.server;

import java.util.*;

public class ServerConfig {
	public int port = 6379;
	public NodeRole role = NodeRole.MASTER;
	public boolean restore = false;
	public String masterHost = null;
	public int masterPort = -1;
}
