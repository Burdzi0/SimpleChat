package com.burdzi0.SimpleChat.service.chat.Server;

import lombok.extern.slf4j.Slf4j;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class Server {

	public static void main(String[] args) throws IOException {
		new Server().serve(new InetSocketAddress(12345));
	}

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
					handleAcception(selector, key);
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
		channel.read(buffer);
		log.info("Read from channel");
		buffer.flip();
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
		System.out.printf("Message: %s%n", charBuffer.toString());
		broadcast(selector, key, charBuffer.toString());
		buffer.clear();
		charBuffer.clear();
	}

	private void handleAcception(Selector selector, SelectionKey key) throws IOException {
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

	private static String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
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
