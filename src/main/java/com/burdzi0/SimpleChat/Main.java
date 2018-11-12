package com.burdzi0.SimpleChat;

import com.burdzi0.SimpleChat.model.Author;
import com.burdzi0.SimpleChat.model.Message;
import com.burdzi0.SimpleChat.repository.AuthorRepository;
import com.burdzi0.SimpleChat.repository.InMemoryAuthorRepository;
import com.burdzi0.SimpleChat.repository.InMemoryMessageRepository;
import com.burdzi0.SimpleChat.repository.MessageRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("IRC");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();

		AuthorRepository authorRepository = new InMemoryAuthorRepository(manager);
		MessageRepository messageRepository = new InMemoryMessageRepository(manager);

		Author author = new Author("Username", "Password");

		Message message = new Message(author, "New Message", new Date());

		author.setMessages(new ArrayList<>() {{add(message);}});

		authorRepository.saveAuthor(author);


		System.out.println("Messages");
		messageRepository.getAllMessages().forEach(System.out::println);
		System.out.println("Authors");
		authorRepository.getAllAuthors().forEach(System.out::println);

		Message message1 = new Message(author, "Second message", new Date());
		messageRepository.saveMessage(message1);

		System.out.println("Authors");
		authorRepository.getAllAuthors().forEach(System.out::println);

		manager.close();
		factory.close();
	}
}
