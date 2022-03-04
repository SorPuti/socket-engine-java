package br.com.github.puti.engine.socket.protocol.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class ByteUtil {
	@SuppressWarnings("finally")
	public static byte[] toByte(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			byte[] arrayBytes = bos.toByteArray();
			return arrayBytes;
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
			}
			return null;
		}

	}

	@SuppressWarnings("finally")
	public static Object toObject(byte[] arrayBytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(arrayBytes);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			return o;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}

			return null;
		}
	}

	@SuppressWarnings({ "finally", "unchecked" })
	public static <R> R toObject(Class<R> clazz, byte[] arrayBytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(arrayBytes);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Object o = in.readObject();
			return (R) o;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}

			return null;
		}
	}

	public static String toSerialize(Object object) {
		String encodedItemStack;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream bukkitObjectOutputStream = null;
		try {
			bukkitObjectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			bukkitObjectOutputStream.writeObject(object);
			bukkitObjectOutputStream.flush();
			byte[] serializedObject = byteArrayOutputStream.toByteArray();

			encodedItemStack = Base64.getEncoder().encodeToString(serializedObject);
			return encodedItemStack;
		} catch (IOException e) {
		}
		return null;
	}

	public static Object deserialize(String code) {
		return deserialize(Object.class, code);
	}

	@SuppressWarnings("unchecked")
	public static <R> R deserialize(Class<R> clazz, String code) {

		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(code));
			ObjectInputStream stream = new ObjectInputStream(byteArrayInputStream);
			return (R) stream.readObject();
		} catch (IOException | ClassNotFoundException e) {
		}

		return null;
	}
}
