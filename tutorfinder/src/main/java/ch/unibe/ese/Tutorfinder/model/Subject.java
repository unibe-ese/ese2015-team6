package ch.unibe.ese.Tutorfinder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Entity for subject, holding following values:<br>
 * {@code id} is the id of the subject and is generated automatically<br>
 * {@code email} is used for referencing between user and subject (same for id)<br>
 * {@code name} name of the subject, is the value referenced to search tutors<br>
 * {@code grade} holds the final value of the subjects note
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Entity
@Table(name = "subject", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user"}) )
public class Subject {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name="user")
	private User user;

	@NotNull
	@Column(name="name")
	private String name;
	
	@NotNull
	private double grade;

	
	/* Getters and Setters */
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

}