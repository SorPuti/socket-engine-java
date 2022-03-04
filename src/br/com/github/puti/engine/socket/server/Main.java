package br.com.github.puti.engine.socket.server;

import br.com.github.puti.engine.socket.protocol.packet.Packet;
import br.com.github.puti.engine.socket.server.client.ServerClient;

public class Main {

	public static void main(String[] args) {

		final Server server = new Server(8080);
		Runnable r = new Runnable() {
			public void run() {
				server.start();
			};
		};
		new Thread(r).start();

		server.onJoin((client) -> {
			ServerClient serverClient = (ServerClient) client;

			serverClient.on("data", (data) -> {
				System.out.println("Recebi: " + ((Packet)data).value);
			});
			
		});
		
		System.out.println("Server OK!");
	}

}
