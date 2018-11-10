package com.burdzi0.SimpleChat.repository;

import com.burdzi0.SimpleChat.model.Message;

import javax.persistence.EntityManager;
import java.util.List;

public class InMemoryMessageRepository implements MessageRepository {

	private EntityManager manager;

	@Override
	public Message findMessageById(Long id) {
		return manager.find(Message.class, id);
	}

	@Override
	public Message findMessageByContent(String partOfContent) {
		return null;
	}

	@Override
	public List<Message> getAllMessages() {
		return null;
	}

	@Override
	public List<Message> getMessagesUpTo(int numberOfMessages) {
		return null;
	}
}
