package ch.unibe.ese.Tutorfinder.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
 * Entity for timetable, holding following values:<br>
 * {@code id} is the id of the appointment and is generated automatically<br>
 * {@code user} is used for referencing between user and timetable)<br>
 * {@code day} of the week (monday, tuesday, etc.)<br>
 * {@code time} of the day (00:00-23:59:59.999999999)<br>
 * {@code availability} true if the tutor is available, else false<br>
 * 
 * @author Antonio
 *
 */
@Entity
@Table(name = "timetable", uniqueConstraints = @UniqueConstraint(columnNames = {"day", "time", "user"}) )
public class Timetable {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="user")
	private User user;
	
	@NotNull
	@Column(name="day")
	private DayOfWeek day;
	
	@NotNull
	@Column(name="time")
	private LocalTime time;
	
	@NotNull
	private Boolean availability;	
	
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
	
	public LocalTime getTime() {
		return time;
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public Boolean getAvailability() {
		return availability;
	}
	
	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}
	
}