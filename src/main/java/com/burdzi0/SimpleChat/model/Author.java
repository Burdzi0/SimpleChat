package com.burdzi0.SimpleChat.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<Message> messages;

	public Author(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Author() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, messages);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) return false;
		Author author = (Author) obj;
		if (!author.id.equals(id)) return false;
		if (!author.username.equals(username)) return false;
		if (!author.password.equals(password)) return false;
		return author.messages.equals(messages);
	}

	@Override
	public String toString() {
		return "Author{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", messages=" + messages +
				'}';
	}
}
