package com.burdzi0.SimpleChat.model;

import com.burdzi0.SimpleChat.listener.Slf4jMessageLogListener;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
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
}
