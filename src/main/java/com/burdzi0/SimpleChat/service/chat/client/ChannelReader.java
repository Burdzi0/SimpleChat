package com.burdzi0.SimpleChat.service.chat.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChannelReader implements Runnable{

	private SocketChannel channel;

	public ChannelReader(SocketChannel channel) {
		this.channel = channel;
		if (!channel.isOpen()) {
			throw new IllegalStateException("ChannelReader: The channel is closed");
		}
	}

	private ByteBuffer read(ByteBuffer buffer) {
		try {
			channel.read(buffer);
			buffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	@Override
	public void run() {
		var byteBuffer = ByteBuffer.allocate(Client.BUFFER_SIZE);
		while (true) {
			read(byteBuffer);
			System.out.println(new String(StandardCharsets.UTF_8.decode(byteBuffer).array()));
			byteBuffer.clear();
		}
	}
}
