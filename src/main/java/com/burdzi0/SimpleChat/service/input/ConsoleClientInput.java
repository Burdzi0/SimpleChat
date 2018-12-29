package com.burdzi0.SimpleChat.service.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

public class ConsoleClientInput implements ClientInput{

	private static Logger log = LogManager.getLogger();
	private BufferedReader reader;

	@Override
	public CharBuffer read() {
		if (reader == null) open();
		String line = readLine();
		return CharBuffer.wrap(line);
	}

	private void open() {
		log.info("Opening BufferedReader");
		reader = new BufferedReader(new InputStreamReader(System.in));
		log.info("Opened BufferedReader");
	}

	private String readLine() {
		String line = "";
		try {
			log.info("Reading line");
			while ((line = reader.readLine()).isBlank()) {}
			log.info("Line read");
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("Returning line: " + line);
		return line;
	}

}
