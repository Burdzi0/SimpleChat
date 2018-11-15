package com.burdzi0.SimpleChat.service.chat.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ChannelReader implements Runnable{

	private SocketChannel channel;

	public ChannelReader(SocketChannel channel) {
		this.channel = channel;
		if (!channel.isConnected()) {
			throw new IllegalStateException("ChannelReader: The channel is closed");
		}
		log.info("Channel should be opened");
	}

	private ByteBuffer read(ByteBuffer buffer) {
		try {
			channel.read(buffer);
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
			if (byteBuffer.position() != 0) {
				byteBuffer.flip();
				System.out.println("Read: " + new String(StandardCharsets.UTF_8.decode(byteBuffer).array()));
				byteBuffer.clear();
			}
		}
	}
}
