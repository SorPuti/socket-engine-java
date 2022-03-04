package br.com.github.puti.engine.socket.server.client;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import br.com.github.puti.engine.socket.components.message.BaseMessageClient;
import br.com.github.puti.engine.socket.functions.Ak;
import br.com.github.puti.engine.socket.protocol.packet.Packet;
import br.com.github.puti.engine.socket.server.Server;

public class ServerClient extends BaseMessageClient {

	private Server server;
	private Map<String, Ak> channels = new HashMap<String, Ak>();
	private String name;

	public ServerClient(Socket client, Server server) {
		super(client);
		this.server = server;
	}

	public void on(String channel, Ak ak) {
		channels.put(channel, ak);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public void onMessage(Packet message) {
		String channel = message.channel;
		if (channel.equals("setName")) {
			this.name = message.value.toString();
			if (this.server.joinEvent != null)
				this.server.joinEvent.action(this);
			return;
		}

		if (channels.containsKey(channel))
			channels.get(channel).action(message);
	}

}
