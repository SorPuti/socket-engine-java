package br.com.github.puti.engine.socket.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ServerSocketFactory;

import br.com.github.puti.engine.socket.functions.Ak;
import br.com.github.puti.engine.socket.logger.Console;
import br.com.github.puti.engine.socket.protocol.packet.Packet;
import br.com.github.puti.engine.socket.server.client.ServerClient;

public class Server {

	private volatile boolean connected = true;
	private List<ServerClient> clients = Collections.synchronizedList(new ArrayList<ServerClient>());
	private Map<String, Ak> channels = new HashMap<String, Ak>();
	private int port;

	public Ak joinEvent = null;
	
	public Server(int port) {
		this.port = port;
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void onJoin(Ak action) {
		this.joinEvent = action;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
		if (!this.connected) {
			for (ServerClient client : this.clients) {
				client.disconnect();
			}
		}
	}

	public void start() {
		try {
			ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(this.port);
			while (server.isBound() && this.isConnected()) {
				Socket client = server.accept();
				ServerClient serverClient = new ServerClient(client, this);
				this.clients.add(serverClient);
			}
		} catch (Exception e) {
			Console.printException(e);
		}
	}

	public void on(String channel, Ak ak) {
		channels.put(channel, ak);
	}

	public void emit(Packet message) {
		for (ServerClient client : this.clients)
			client.writeMessage(message);
	}

}
