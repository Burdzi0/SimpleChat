package com.burdzi0.SimpleChat.service.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class ServerChannel implements Runnable {

	private final String channelName;

	private static Logger log = LogManager.getLogger();
	private final InetSocketAddress address;
	private Selector selector;
	private AsynchronousFileChannel fileChannel;
	private Long position = 0L;

	public ServerChannel(String channelName, InetSocketAddress address) {
		this.channelName = channelName;
		this.address = address;
	}

	public static void main(String[] args) {
		new ServerChannel("Channel", new InetSocketAddress("localhost", 12345)).run();
	}

	@Override
	public void run() {
		try {
			fileChannel = AsynchronousFileChannel.open(Path.of(getNewLogFileName()), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			serve(address);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getNewLogFileName() {
		return fileChannel + LocalDateTime.now().format(ISO_LOCAL_DATE) + ".txt";
	}

	public void serve(InetSocketAddress address) {
		ServerSocketChannel serverSocketChannel = null;
		try {
			serverSocketChannel = prepareServerSocketChannel(address);
			selector = createSelector(serverSocketChannel);

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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocketChannel != null) {
					serverSocketChannel.close();
				}
			} catch (IOException e) {
			}
		}
	}

	private Selector createSelector(ServerSocketChannel serverSocketChannel) throws IOException {
		Selector selector = Selector.open();
		log.info("Created selector");
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		log.info("Registered server socket channel");
		return selector;
	}

	private ServerSocketChannel prepareServerSocketChannel(InetSocketAddress address) throws IOException {
		var serverSocketChannel = ServerSocketChannel.open();
		log.info("Opened server socket channel");
		serverSocketChannel.configureBlocking(false);
		log.info("Configured channel to nonblocking mode");

		serverSocketChannel.bind(address);
		log.info("Bind to port: " + address.getPort());
		return serverSocketChannel;
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
			fileChannel.write(buffer, position);
			position = +(long) buffer.position();
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

	public void registerClient(SocketChannel socket) {
		try {
			socket.configureBlocking(false);
			socket.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
