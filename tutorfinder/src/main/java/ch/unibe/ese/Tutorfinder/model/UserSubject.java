package ch.unibe.ese.Tutorfinder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * 
 * @author Florian
 *Entity describing which tutors have passed which subjects
 */

@Entity
public class UserSubject {
	
	@Id
	@GeneratedValue
	private long id;
		
	@ManyToOne
	private User user;
	
	private String subject;
	private int mark;
	
	
	
	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	
	

}
