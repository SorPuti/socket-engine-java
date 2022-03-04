package br.com.github.puti.engine.socket.components.writer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.github.puti.engine.socket.components.message.MessageClient;
import br.com.github.puti.engine.socket.logger.Console;
import br.com.github.puti.engine.socket.protocol.packet.ByteUtil;
import br.com.github.puti.engine.socket.protocol.packet.Packet;

public class MessageWriter implements Runnable {

	private MessageClient client;
	private DataOutputStream stream;
	private BlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();
	private volatile boolean connected = true;
	private String author;

	public MessageWriter(MessageClient client, OutputStream stream) throws IOException {
		this.client = client;
		this.stream = new DataOutputStream(stream);
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void writeMessage(Object message) {
		this.queue.offer(new Packet("data", message, author));
	}

	public void writeMessage(String channel, Object message) {
		this.queue.offer(new Packet(channel, message, author));
	}

	public boolean isText(Object object) {
		if (object instanceof String)
			return true;

		return false;
	}

	@Override
	public void run() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {
			try {

				Packet object = this.queue.poll(1, TimeUnit.SECONDS);
				if (object == null)
					return;

				byte[] bytes = ByteUtil.toSerialize(object).getBytes();

				this.stream.writeInt(bytes.length);
				this.stream.write(bytes);
			} catch (InterruptedException e) {
				Console.printException(e);
			} catch (IOException e) {
				this.client.errorOnWrite(e);
			}
		};
		executor.scheduleWithFixedDelay(task, 0, 20l * 60, TimeUnit.MILLISECONDS);
	}

}
