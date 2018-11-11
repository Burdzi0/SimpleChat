package com.burdzi0.SimpleChat.model;

import com.burdzi0.SimpleChat.listener.Slf4jMessageLogListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(Slf4jMessageLogListener.class)
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Author author;
	private String content;

	@Column(name = "sending_timestamp")
	private Date sendingTime;

	public Message(Author author, String content, Date sendingTime) {
		this.author = author;
		this.content = content;
		this.sendingTime = sendingTime;
	}

	public Message() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendingTime() {
		return sendingTime;
	}

	public void setSendingTime(Date sendingTime) {
		this.sendingTime = sendingTime;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) return false;
		Message objDate = (Message) obj;
		if (!objDate.getId().equals(id)) return false;
		if (!objDate.getSendingTime().equals(sendingTime)) return false;
		if (!objDate.author.equals(author)) return false;
		return objDate.getContent().equals(content);
	}

	@Override
	public int hashCode() {
		return id.hashCode() * author.hashCode() * content.hashCode() * sendingTime.hashCode();
	}

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", author=" + author.getUsername() +
				", content='" + content + '\'' +
				", sendingTime=" + sendingTime +
				'}';
	}
}
