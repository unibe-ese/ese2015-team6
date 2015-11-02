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
 * Entity for timetable, holding following values:<br>
 * {@code id} is the id of the appointment and is generated automatically<br>
 * {@code user} is used for referencing between user and timetable)<br>
 * {@code day} of the week (Monday, Tuesday, etc.)<br>
 * {@code time} of the day (00:00-23:59:59.999999999)<br>
 * {@code availability} true if the tutor is available, else false<br>
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Entity
@Table(name = "timetable", uniqueConstraints = @UniqueConstraint(columnNames = {"day", "timeslot", "user"}) )
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
	@Column(name="timeslot")
	private int timeslot;
	
	@NotNull
	private Boolean availability;	
	
	/* Constructors */
	public Timetable () {
		super();
	}
	
	public Timetable (User user, DayOfWeek day, int timeslot, Boolean availability) {
		super();
		this.user = user;
		this.day = day;
		this.timeslot = timeslot;
		this.availability = availability;
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
		this.user = user;
	}
	
	public DayOfWeek getDay() {
		return day;
	}
	
	public void setDay(DayOfWeek day) {
		this.day = day;
	}
	
	public int getTime() {
		return timeslot;
	}
	
	public void setTime(int time) {
		this.timeslot = time;
	}
	
	public Boolean getAvailability() {
		return availability;
	}
	
	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}
	
}