package com.burdzi0.SimpleChat.service.chat;

public enum BinaryUnit {
	BYTES(1),
	KILOBYTES(1024),
	MEGABYTES(1024 * 1024),
	GIGABYTES(1024 * 1024 * 1024);

	private int capacity;

	BinaryUnit(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getName() {
		switch (this) {
			case BYTES: return "Bytes";
			case KILOBYTES: return "Kilobytes";
			case MEGABYTES: return "Megabytes";
			case GIGABYTES: return "Gigabytes";
			default: return "";
		}
	}
}
