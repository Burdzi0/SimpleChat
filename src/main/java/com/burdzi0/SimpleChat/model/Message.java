package com.burdzi0.SimpleChat.model;

import com.burdzi0.SimpleChat.listener.Slf4jMessageLogListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(Slf4jMessageLogListener.class)
@NamedQueries({
		@NamedQuery(
				name = "Message.All",
				query = "SELECT m FROM Message m"
		),
		@NamedQuery(
				name = "Message.FindByContent",
				query = "SELECT m FROM Message m WHERE m.content like :content"
		)
})
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "author_fk")
	private Author author;
	private String content;

	@Column(name = "sending_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendingTime;

	public Message(Author author, String content, Date sendingTime) {
		this.author = author;
		this.content = content;
		this.sendingTime = sendingTime;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", author=" + author.getId() +
				", content='" + content + '\'' +
				", sendingTime=" + sendingTime +
				'}';
	}
}
