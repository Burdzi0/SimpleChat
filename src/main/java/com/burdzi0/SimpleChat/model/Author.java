package com.burdzi0.SimpleChat.model;

import com.burdzi0.SimpleChat.listener.Slf4jAuthorLogListener;
import com.burdzi0.SimpleChat.listener.Slf4jMessageLogListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(Slf4jAuthorLogListener.class)
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
}
