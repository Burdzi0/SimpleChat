package com.burdzi0.SimpleChat.service.dispatcher;

import com.burdzi0.SimpleChat.service.chat.server.ServerChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Dispatcher {

	private static Logger log = LogManager.getLogger();
	private ServerSocketChannel serverChannel;
	private Map<String, ServerChannel> channelMap = new HashMap<>(5);

	public void receiveAndDispatch() throws IOException {
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		while (true) {
			var socket = serverChannel.accept();

			var byteBuffer = ByteBuffer.allocate(1024);

			var readLength = socket.read(byteBuffer);
			log.info("Read %d bytes from %s", readLength, socket.getRemoteAddress());

			var message = StandardCharsets.UTF_8.decode(byteBuffer).toString();
			log.info("[%s] %s", socket.getRemoteAddress(), message);

			Optional.ofNullable(channelMap.get(message))
					.ifPresentOrElse(
							ch -> ch.registerClient(socket),
							() -> {
								try {
									socket.write(StandardCharsets.UTF_8.encode(CharBuffer.wrap("Channel does not exist")));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
					);
		}

	}

}
