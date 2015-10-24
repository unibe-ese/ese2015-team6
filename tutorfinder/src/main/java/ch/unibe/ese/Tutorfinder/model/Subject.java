package ch.unibe.ese.Tutorfinder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Entity for subject, holding following values:<br>
 * {@code id} is the id of the subject and is generated automatically<br>
 * {@code email} is used for referencing between user and subject (same for id)<br>
 * {@code subject} name of the subject, is the value referenced to search tutors<br>
 * {@code note} holds the final value of the subjects note
 * 
 * @author Antonio
 *
 */
@Entity
public class Subject {

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private String email;
	private String subject;
	private double note;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public double getNote() {
		return note;
	}
	
	public void setNote(double note) {
		this.note = note;
	}	
	
}
