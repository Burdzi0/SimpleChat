package com.burdzi0.SimpleChat.service.chat.client;

import com.burdzi0.SimpleChat.service.input.ClientInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChannelWriter implements Runnable {

	private static Logger log = LogManager.getLogger();
	private SocketChannel channel;
	private ClientInput input;
	private String author;

	public ChannelWriter(SocketChannel channel, ClientInput input) {
		this.channel = channel;
		this.input = input;
		if (!channel.isOpen()) {
			throw new IllegalStateException("ChannelReader: The channel is closed");
		}
		log.info("Channel should be opened");
	}

	private void write(CharBuffer charBuffer) {
		log.info("Trying to write");
		try {
			channel.write(StandardCharsets.UTF_8.encode(author + ": " + charBuffer));
			log.info("Written");
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
//			charBuffer.flip();
			write(charBuffer);
		}
	}

	public void setAuthor(String authorName) {
		this.author = authorName;
	}
}
