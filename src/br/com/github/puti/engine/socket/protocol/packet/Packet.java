package br.com.github.puti.engine.socket.protocol.packet;

import java.io.Serializable;

public class Packet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String channel;
	public Object value;
	public String author;

	public Packet(String channel, Object value) {
		this(channel, value, null);
	}

	public Packet(String channel, Object value, String author) {
		this.channel = channel;
		this.author = author;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public <R> R asCast(Class<R> clazz) {
		return (R) value;
	}

	public String asText() {
		return value != null && value instanceof String ? String.valueOf(value) : null;
	}

	public Integer asInteger() {
		return Integer.parseInt(this.asText());
	}

	public Double asDouble() {
		return Double.parseDouble(this.asText());
	}

	public Float asFloat() {
		return Float.parseFloat(this.asText());
	}

	public void print() {
		System.out.println("Author: " + this.author);
		System.out.println("Channel: " + this.channel);
	}

}
