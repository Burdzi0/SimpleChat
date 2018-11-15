package com.burdzi0.SimpleChat.service.chat.client;

import com.burdzi0.SimpleChat.service.input.ClientInput;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChannelWriter implements Runnable {

	private SocketChannel channel;
	private ClientInput input;

	public ChannelWriter(SocketChannel channel, ClientInput input) {
		this.channel = channel;
		if (!channel.isOpen()) {
			throw new IllegalStateException("ChannelReader: The channel is closed");
		}
	}

	private void write(CharBuffer charBuffer) {
		try {
			channel.write(StandardCharsets.UTF_8.encode(charBuffer));
			charBuffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		CharBuffer charBuffer;
		while (true) {
			charBuffer = input.read();
			charBuffer.flip();
			write(charBuffer);
		}
	}
}
