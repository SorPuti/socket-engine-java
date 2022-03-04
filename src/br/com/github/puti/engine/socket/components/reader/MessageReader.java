package br.com.github.puti.engine.socket.components.reader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.github.puti.engine.socket.components.message.MessageClient;
import br.com.github.puti.engine.socket.protocol.packet.ByteUtil;
import br.com.github.puti.engine.socket.protocol.packet.Packet;

public class MessageReader implements Runnable {

	private static final Executor POOL = Executors.newFixedThreadPool(16);

	final private MessageClient client;
	private DataInputStream stream;
	private boolean connected = true;
	private String author;

	public MessageReader(MessageClient client, InputStream stream) throws IOException {
		this.client = client;
		System.out.println(stream);
		this.stream = new DataInputStream(stream);
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@Override
	public void run() {
		try {
			while (this.isConnected()) {
				int size = this.stream.readInt();
				byte[] message = new byte[size];
				for (int x = 0; x < size; x++) {
					message[x] = (byte) this.stream.read();
				}

				Packet packet = ByteUtil.deserialize(Packet.class, new String(message));
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						client.onMessage(packet);
					}
				};
				POOL.execute(runnable);
			}
		} catch (Exception e) {
			this.client.errorOnRead(e);
//			throw new RuntimeException(e);
		}

	}

}
