package com.burdzi0.SimpleChat.repository;

import com.burdzi0.SimpleChat.model.Message;

import java.util.List;

public interface MessageRepository {
	void saveMessage(Message message);
	Message findMessageById(Long id);
	Message findMessageByContent(String partOfContent);
	List<Message> getAllMessages();
	List<Message> getMessagesUpTo(int numberOfMessages);
}
