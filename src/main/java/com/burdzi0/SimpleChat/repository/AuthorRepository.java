package com.burdzi0.SimpleChat.repository;

import com.burdzi0.SimpleChat.model.Author;

import java.util.List;

public interface AuthorRepository {
	void saveAuthor(Author author);
	Author findAuthorById(Long id);
	List<Author> findAuthorByUsername(String username);
	List<Author> getAllAuthors();
	List<Author> getAuthorsUpTo(int numberOfAuthors);
}
