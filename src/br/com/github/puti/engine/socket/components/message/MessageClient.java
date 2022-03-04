package br.com.github.puti.engine.socket.components.message;

import br.com.github.puti.engine.socket.protocol.packet.Packet;

public interface MessageClient {

	public void errorOnWrite(Exception e);

	public void errorOnRead(Exception e);

	public void onMessage(Packet message);

}
