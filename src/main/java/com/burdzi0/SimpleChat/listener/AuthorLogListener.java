package com.burdzi0.SimpleChat.listener;

import com.burdzi0.SimpleChat.model.Author;

public interface AuthorLogListener {
	void logOnSave(Author author);
	void logOnPreUpdate(Author author);
	void logOnPostUpdate(Author author);
	void logOnDelete(Author author);
	void logOnSearch(Author author);
}
