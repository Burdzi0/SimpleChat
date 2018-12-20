package com.burdzi0.SimpleChat.service.chat.client;

import com.burdzi0.SimpleChat.service.chat.BinaryUnit;
import com.burdzi0.SimpleChat.service.input.ClientInput;
import com.burdzi0.SimpleChat.service.input.ConsoleClientInput;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

	private String authorName;
	protected static final int BUFFER_SIZE = 5 * BinaryUnit.MEGABYTES.getCapacity();
	private ExecutorService service = Executors.newCachedThreadPool();

	public Client(String authorName) {
		this.authorName = authorName;
	}

	public void runClient() throws IOException {
		var channel = SocketChannel.open(new InetSocketAddress(12345));
		channel.configureBlocking(true);
		service.execute(new ChannelReader(channel));
		ChannelWriter writer = new ChannelWriter(channel, new ConsoleClientInput());
		writer.setAuthor(authorName);
		service.execute(writer);
	}

	public static void main(String[] args) throws IOException {
		new Client("ABC").runClient();
	}
}
