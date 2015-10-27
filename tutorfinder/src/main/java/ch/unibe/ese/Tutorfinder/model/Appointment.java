package ch.unibe.ese.Tutorfinder.model;

import java.time.DayOfWeek;

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
 * Entity for appointment, holding following values:<br>
 * {@code id} is the id of the appointment and is generated automatically<br>
 * {@code user} is used for referencing between user and subject (same for id)<br>
 * {@code day} of the week (monday, tuesday, etc.)<br>
 * 
 * @author Antonio
 *
 */
@Entity
@Table(name = "appointment", uniqueConstraints = @UniqueConstraint(columnNames = {"day", "user"}) )
public class Appointment {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="user")
	private User user;
	
	@NotNull
	@Column(name="day")
	private DayOfWeek day;
	
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
	
	public DayOfWeek getDay() {
		return day;
	}
	
	public void setDay(DayOfWeek day) {
		this.day = day;
	}
	
}