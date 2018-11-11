package com.burdzi0.SimpleChat.listener;

import com.burdzi0.SimpleChat.model.Message;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

@Slf4j
public class Slf4jMessageLogListener implements MessageLogListener {

	@PostPersist
	@Override
	public void logOnSave(Message message) {
		log.info("Saved message: " + message);
	}

	@PostLoad
	@Override
	public void logOnLoad(Message message) {
		log.info("Queried message: " + message);
	}
}
