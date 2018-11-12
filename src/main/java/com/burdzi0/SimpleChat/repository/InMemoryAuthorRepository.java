package com.burdzi0.SimpleChat.repository;

import com.burdzi0.SimpleChat.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class InMemoryAuthorRepository implements AuthorRepository{

	private EntityManager manager;
	private EntityTransaction transaction;

	public InMemoryAuthorRepository(EntityManager manager) {
		this.manager = manager;
		transaction = manager.getTransaction();
	}

	@Override
	public void saveAuthor(Author author) {
		transaction.begin();
		manager.persist(author);
		transaction.commit();
	}

	@Override
	public Author findAuthorById(Long id) {
		return manager.find(Author.class, id);
	}

	@Override
	public List<Author> findAuthorByUsername(String username) {
		TypedQuery<Author> authorsQuery = manager.createNamedQuery("Author.ByUsername", Author.class);
		authorsQuery.setParameter("username", username);
		return authorsQuery.getResultList();
	}

	@Override
	public List<Author> getAllAuthors() {
		TypedQuery<Author> authorsQuery = manager.createNamedQuery("Author.All", Author.class);
		return authorsQuery.getResultList();
	}

	@Override
	public List<Author> getAuthorsUpTo(int numberOfAuthors) {
		TypedQuery<Author> authorsQuery = manager.createNamedQuery("All", Author.class);
		authorsQuery.setMaxResults(numberOfAuthors);
		return authorsQuery.getResultList();
	}
}
