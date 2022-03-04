package br.com.github.puti.engine.socket.components.message;

import java.net.Socket;

import br.com.github.puti.engine.socket.components.reader.MessageReader;
import br.com.github.puti.engine.socket.components.writer.MessageWriter;
import br.com.github.puti.engine.socket.logger.Console;
import br.com.github.puti.engine.socket.protocol.packet.Packet;

public abstract class BaseMessageClient implements MessageClient {

	private Socket socket;
	private MessageReader reader;
	private MessageWriter writer;
	private String author = null;

	public BaseMessageClient(String host, int port) throws Exception {
		this(new Socket(host, port));
	}

	public void setAuthor(String author) {
		this.author = author;
		this.reader.setAuthor(author);
		this.writer.setAuthor(author);
	}

	public String getAuthor() {
		return author;
	}

	public BaseMessageClient(Socket socket) {
		try {
			this.socket = socket;
			this.reader = new MessageReader(this, this.socket.getInputStream());
			this.writer = new MessageWriter(this, this.socket.getOutputStream());
			new Thread(this.reader).start();
			new Thread(this.writer).start();
		} catch (Exception e) {
			Console.printException(e);
		}

	}

	public MessageReader getReader() {
		return reader;
	}

	public MessageWriter getWriter() {
		return writer;
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public void errorOnWrite(Exception e) {
		Console.log("Ocorreu um erro ao escrever");
		Console.printException(e);
	}

	public void writeMessage(String message) {
		this.getWriter().writeMessage(message);
	}

	public void writeMessage(String channel, Object message) {
		this.getWriter().writeMessage(channel, message);
	}

	public void writeMessage(Packet message) {
		this.getWriter().writeMessage(message);
	}

	@Override
	public void errorOnRead(Exception e) {
		System.out.println("Ocorreu um erro durante a leitura");
		Console.printException(e);
	}

	public void disconnect() {
		this.writeMessage("disconnect");
		this.reader.setConnected(false);
		this.writer.setConnected(false);
		try {
			Thread.sleep(1000);
			this.socket.close();
		} catch (Exception e) {
			Console.printException(e);
//            throw new RuntimeException(e);
		}
	}

}
