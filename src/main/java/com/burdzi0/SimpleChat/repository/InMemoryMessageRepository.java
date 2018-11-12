package com.burdzi0.SimpleChat.repository;

import com.burdzi0.SimpleChat.model.Message;

import javax.persistence.*;
import java.util.List;

public class InMemoryMessageRepository implements MessageRepository {

	private EntityManager manager;
	private EntityTransaction transaction;

	public InMemoryMessageRepository(EntityManager manager) {
		this.manager = manager;
		transaction = manager.getTransaction();
	}

	@Override
	public void saveMessage(Message message) {
		transaction.begin();
		manager.persist(message);
		transaction.commit();
	}

	@Override
	public Message findMessageById(Long id) {
		return manager.find(Message.class, id);
	}

	@Override
	public List<Message> findMessageByContent(String partOfContent) {
		TypedQuery<Message> messageQuery = manager.createNamedQuery("Message.FindByContent", Message.class);
		messageQuery.setParameter("content", partOfContent);
		return messageQuery.getResultList();
	}

	@Override
	public List<Message> getAllMessages() {
		TypedQuery<Message> messageQuery = manager.createNamedQuery("Message.All", Message.class);
		return messageQuery.getResultList();
	}

	@Override
	public List<Message> getMessagesUpTo(int numberOfMessages) {
		TypedQuery<Message> messageQuery = manager.createNamedQuery("All", Message.class);
		messageQuery.setMaxResults(numberOfMessages);
		return messageQuery.getResultList();
	}
}
