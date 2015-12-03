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
 * @version	1.0
 *
 */
@Entity
@Table(name = "timetable", uniqueConstraints = @UniqueConstraint(columnNames = {"day", "timeslot", "user"}) )
public class Timetable {

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="user")
	private User user;
	
	@NotNull
	@Column(name="day")
	private DayOfWeek day;
	
	@NotNull
	@Column(name="timeslot")
	private int timeslot;
	
	/* Constructors */
	public Timetable () {
		super();
	}
	
	public Timetable (User user, DayOfWeek day, int timeslot) {
		super();
		this.user = user;
		this.day = day;
		this.timeslot = timeslot;
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
		assert user != null : "Timetables user is not allowed to be null!";
		this.user = user;
	}
	
	public DayOfWeek getDay() {
		return day;
	}
	
	public void setDay(DayOfWeek day) {
		assert day != null : "Timetables day is not allowed to be null!";
		this.day = day;
	}
	
	public int getTime() {
		return timeslot;
	}
	
	public void setTime(int time) {
		this.timeslot = time;
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
		Timetable other = (Timetable) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Timetable [id=" + id + ", user=" + user + ", day=" + day + ", timeslot=" + timeslot + "]";
	}
	
}