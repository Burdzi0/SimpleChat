package com.burdzi0.SimpleChat;

import com.burdzi0.SimpleChat.model.Author;
import com.burdzi0.SimpleChat.model.Message;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("IRC");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();

		Author author = new Author("User", "Password");

		transaction.begin();
		manager.persist(author);
		transaction.commit();

		transaction.begin();
		manager.persist(new Message(manager.find(Author.class, 1L), "Hello world", new Date()));
		transaction.commit();

//		manager.flush();

		TypedQuery<Message> messageTypedQuery = manager.createQuery(
				"select m from Message m",
				Message.class
		);

		TypedQuery<Author> authorTypedQuery = manager.createQuery(
				"select m from Author m",
				Author.class
		);

		authorTypedQuery.getResultList()
				.stream()
				.map(Author::toString)
				.forEach(System.out::println);

		messageTypedQuery.getResultList()
				.stream()
				.map(Message::toString)
				.forEach(System.out::println);

		manager.close();
		factory.close();
	}
}
