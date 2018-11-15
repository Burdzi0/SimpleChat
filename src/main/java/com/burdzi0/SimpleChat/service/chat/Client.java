package com.burdzi0.SimpleChat.service.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

	public static void main(String[] args) throws IOException {
		var socketChannel = SocketChannel.open(new InetSocketAddress(12348));
		var charBuffer = CharBuffer.allocate(1024);
		charBuffer.put("REQUEST").flip();
		System.out.println("Sending request");
		socketChannel.write(StandardCharsets.UTF_8.encode(charBuffer));
		var byteBuffer = ByteBuffer.allocate(1024);
		socketChannel.read(byteBuffer);
		byteBuffer.flip();
		System.out.println(new String(StandardCharsets.UTF_8.decode(byteBuffer).array()));
		socketChannel.close();
	}
}
