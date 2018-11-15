package com.burdzi0.SimpleChat.service.input;

import java.io.Console;
import java.nio.CharBuffer;

public class ConsoleClientInput implements ClientInput{

	private Console console;

	@Override
	public CharBuffer read() {
		if (console == null) {
			console = System.console();
		}
		return CharBuffer.wrap(console.readLine());
	}

}
