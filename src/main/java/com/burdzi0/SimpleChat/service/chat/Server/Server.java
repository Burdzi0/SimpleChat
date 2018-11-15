package com.burdzi0.SimpleChat.service.chat.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class Server {

	public static void main(String[] args) throws IOException {
		var serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);

		serverSocketChannel.bind(new InetSocketAddress(12348));

		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		Set<SocketChannel> openedChannels = new CopyOnWriteArraySet<>();

		while (true) {
			System.out.println("We are waiting for events...");
			var numberOfEvents = selector.select();
			System.out.printf("Number of events: %d\n", numberOfEvents);

			Set<SelectionKey> selectedKeys = selector.selectedKeys();

			for (SelectionKey key: selectedKeys) {
				if (key.isAcceptable()) {
					System.out.println("Accepting the connection");
					ServerSocketChannel channel = (ServerSocketChannel) key.channel();
					SocketChannel socketChannel = channel.accept();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
					selectedKeys.remove(key);
				} else if (key.isReadable()) {
					System.out.println("Reading the connected SocketChannel");
					SocketChannel channel = (SocketChannel) key.channel();
					var buffer = ByteBuffer.allocate(1024);
					channel.read(buffer);
					buffer.flip();
					CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
					System.out.printf("Message: %s", charBuffer.toString());
					channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
					broadcast(openedChannels, charBuffer.toString());
					buffer.clear();
					openedChannels.add(channel);
					selectedKeys.remove(key);
					if (!openedChannels.contains(channel)){
						channel.register(selector, SelectionKey.OP_WRITE);
					}
				} else if (key.isWritable()) {
					System.out.println("Writing to the connected SocketChannel");
					SocketChannel channel = (SocketChannel) key.channel();
					var charBuffer = CharBuffer.allocate(1024);
					charBuffer.put("RESPONSE").flip();
					channel.write(StandardCharsets.UTF_8.encode(charBuffer));
					System.out.println("Response sent");
					charBuffer.clear();
					selectedKeys.remove(key);
//					key.cancel();
//					channel.close();
				}
			}
		}
	}

	public static void broadcast(Set<SocketChannel> channels, String message) throws IOException {
		for (SocketChannel channel: channels) {
			channel.write(StandardCharsets.UTF_8.encode(message));
		}
	}
}
