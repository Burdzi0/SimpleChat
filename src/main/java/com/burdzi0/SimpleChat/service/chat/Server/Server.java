package com.burdzi0.SimpleChat.service.chat.Server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class Server {

	public void serve(InetSocketAddress address) throws IOException {
		var serverSocketChannel = ServerSocketChannel.open();
		log.info("Opened server socket channel");
		serverSocketChannel.configureBlocking(false);
		log.info("Configured channel to nonblocking mode");

		serverSocketChannel.bind(address);
		log.info("Bind to port: " + address.getPort());

		Selector selector = Selector.open();
		log.info("Created selector");
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		log.info("Registered server socket channel");

		Set<SelectionKey> selectedKeys;
		int selected;
		Iterator<SelectionKey> iter;

		log.info("Listening...");

		while (true) {
			log.info("- Select -");
			selected = selector.select();
			log.info("Selected: " + selected + " channels");

			selectedKeys = selector.selectedKeys();
			iter = selectedKeys.iterator();

			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				iter.remove();
				log.info("Processing key " + key.toString());

				if (key.isAcceptable()) {
					handleAcceptation(selector, key);
				}
				if (key.isReadable()) {
					handleRead(selector, key);
				}
			}
		}
	}

	private void handleRead(Selector selector, SelectionKey key) throws IOException {
		log.info("Key is readable");
		var channel = (SocketChannel) key.channel();
		log.info("Obtained channel");
		var buffer = ByteBuffer.allocate(1024);
		var read = channel.read(buffer);
		if (read == -1) {
			disconnectClient(key, channel);
		} else {
			var message = readData(buffer);
			broadcast(selector, key, message);
		}
	}

	private String readData(ByteBuffer buffer) {
		log.info("Read from channel");
		buffer.flip();
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
		var message = charBuffer.toString();
		System.out.printf("%s%n", message);
		buffer.clear();
		charBuffer.clear();
		return message;
	}

	private void disconnectClient(SelectionKey key, SocketChannel channel) throws IOException {
		log.info("Client [" + channel.getRemoteAddress() + "] disconnected");
		key.cancel();
	}

	private void handleAcceptation(Selector selector, SelectionKey key) throws IOException {
		log.info("Key is acceptable");
		var serverChannel = (ServerSocketChannel) key.channel();
		log.info("Obtained server channel");
		var channel = serverChannel.accept();
		log.info("Obtained channel " + channel.getRemoteAddress());
		channel.configureBlocking(false);
		log.info("Configured channel to nonblocking mode");
		channel.register(selector, SelectionKey.OP_READ);
		log.info("Registered channel as readable");
		channel.write(StandardCharsets.UTF_8.encode("Hello!"));
		log.info("Processing complete");
	}

	private void broadcast(Selector selector, SelectionKey author, String msg) throws IOException {
		ByteBuffer messageBuffer = ByteBuffer.wrap(msg.getBytes());
		for (SelectionKey key : selector.keys()) {
			if (!key.equals(author) && key.isValid() && key.channel() instanceof SocketChannel) {
				SocketChannel channel = (SocketChannel) key.channel();
				channel.write(messageBuffer);
				messageBuffer.rewind();
			}
		}
	}
}
