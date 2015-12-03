package ch.unibe.ese.Tutorfinder.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Entity for messages, holding following values:<br>
 * {@code id} is the id of the appointment and is generated automatically<br>
 * {@code sender} is used for referencing to the sender of the message<br>
 * {@code receiver} is used for referencing to the receiver of the message<br>
 * {@code timestamp} is used to show when the message was send from the sender<br>
 * {@code message} contains a message of the length 1048<br>
 * {@code isRead} indicates whether the message is read by the receiver or not<br>
 * 
 * @version	1.0
 *
 */
@Entity
public class Message {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(name = "timestamp")
	private Timestamp timestamp;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "sender")
	private User sender;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "receiver")
	private User receiver;

	@NotNull
	private String subject;
	
	@NotNull
	@Lob
	private String message;

	@NotNull
	private boolean isRead = false;

	/* Getters and Setters */
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		assert timestamp != null : "Messages timestamp is not allowed to be null!";
		this.timestamp = timestamp;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		assert sender != null : "Messages sender is not allowed to be null!";
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		assert receiver != null : "Messages receiver is not allowed to be null!";
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		assert subject != null : "Messages subject is not allowed to be null!";
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		assert message != null : "Messages message is not allowed to be null!";
		this.message = message;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", timestamp=" + timestamp + ", sender=" + sender + ", receiver=" + receiver
				+ ", message=" + message + ", isRead=" + isRead + "]";
	}

}
