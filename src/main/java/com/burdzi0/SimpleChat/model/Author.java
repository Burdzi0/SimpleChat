package com.burdzi0.SimpleChat.model;

import com.burdzi0.SimpleChat.listener.Slf4jAuthorLogListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(Slf4jAuthorLogListener.class)
@NamedQueries({
		@NamedQuery(
				name = "Author.ByUsername",
				query = "SELECT a FROM Author a WHERE a.username LIKE :username"
		),
		@NamedQuery(
				name = "Author.All",
				query = "SELECT a FROM Author a"
		)
})
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Message> messages;

	public Author(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Author(String username, String password, List<Message> messages) {
		this.username = username;
		this.password = password;
		this.messages = messages;
	}
}
