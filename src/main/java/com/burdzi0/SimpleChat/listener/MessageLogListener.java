package com.burdzi0.SimpleChat.listener;

import com.burdzi0.SimpleChat.model.Message;

public interface MessageLogListener {
	void logOnSave(Message message);
	void logOnLoad(Message message);
}
