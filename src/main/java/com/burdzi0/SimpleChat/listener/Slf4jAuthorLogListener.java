package com.burdzi0.SimpleChat.listener;

import com.burdzi0.SimpleChat.model.Author;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
public class Slf4jAuthorLogListener implements AuthorLogListener{

	@PostPersist
	@Override
	public void logOnSave(Author author) {
		log.info("Saved author: " + author);
	}

	@PreUpdate
	@Override
	public void logOnPreUpdate(Author author) {
		log.info("Updating author: " + author);
	}

	@PostUpdate
	@Override
	public void logOnPostUpdate(Author author) {
		log.info("Updated author to: " + author);
	}

	@PostRemove
	@Override
	public void logOnDelete(Author author) {
		log.info("Removed author: " + author);
	}

	@PostLoad
	@Override
	public void logOnSearch(Author author) {
		log.info("Queried author: " + author);
	}
}
