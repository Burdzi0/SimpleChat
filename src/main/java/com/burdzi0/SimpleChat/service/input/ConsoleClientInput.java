package com.burdzi0.SimpleChat.service.input;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

@Slf4j
public class ConsoleClientInput implements ClientInput{

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
			line = reader.readLine();
			log.info("Line read");
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("Returning line: " + line);
		return line;
	}

}
