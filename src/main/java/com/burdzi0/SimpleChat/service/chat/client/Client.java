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

	protected static final int BUFFER_SIZE = 5 * BinaryUnit.MEGABYTES.getCapacity();
	private ExecutorService service = Executors.newFixedThreadPool(2);

	public void runClient() throws IOException {
		var channel = SocketChannel.open(new InetSocketAddress(12348));
		channel.configureBlocking(true);
		ClientInput input = new ConsoleClientInput();
		new Thread(new ChannelReader(channel)).start();
		new Thread(new ChannelWriter(channel, new ConsoleClientInput())).start();
	}

	public static void main(String[] args) throws IOException {
		new Client().runClient();
	}
}
