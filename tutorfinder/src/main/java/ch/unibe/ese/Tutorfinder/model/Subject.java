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
 * @version	2.0
 *
 */
@Entity
@Table(name = "subject", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user"}) )
public class Subject {

	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name="user")
	private User user;

	@NotNull
	@Column(name="name")
	private String name;
	
	@NotNull
	private double grade;

	/* Constructor */
	public Subject() {
		super();
	}
	
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
		assert user != null : "Subjects user is not allowed to be null!";
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		assert name != null : "Subjects name is not allowed to be null!";
		this.name = name;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Subject other = (Subject) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Subject [id=" + id + ", name=" + name + ", grade=" + grade + "]\n" + user + "\n";
	}

}