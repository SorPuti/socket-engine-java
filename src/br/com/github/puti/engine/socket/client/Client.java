
package br.com.github.puti.engine.socket.client;

import java.util.HashMap;
import java.util.Map;

import br.com.github.puti.engine.socket.components.message.BaseMessageClient;
import br.com.github.puti.engine.socket.functions.Ak;
import br.com.github.puti.engine.socket.protocol.packet.Packet;

public class Client extends BaseMessageClient {

	private Map<String, Ak> channels = new HashMap<>();;
	private String name;

	public Client(String host, String name) throws Exception {
		super(host, 8080);
		this.name = name;
		setAuthor(name);
	}

	public String getName() {
		return name;
	}

	public void on(String channel, Ak ak) {
		channels.put(channel, ak);
		if (channel.equals("read"))
			this.onMessage(new Packet("read", null, ""));
	}

	public void onMessage(Packet message) {
		if (message.author == null || message.author.equals(name))
			return;

		String channel = message.channel;

		if (channel.equals("read"))
			this.writeMessage("setName", name);
		if (channels.containsKey(channel))
			channels.get(channel).action(message);
	}

}
