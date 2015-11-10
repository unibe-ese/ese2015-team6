package ch.unibe.ese.Tutorfinder.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import ch.unibe.ese.Tutorfinder.util.Availability;
/**
 * Entity for timetable, holding following values:<br>
 * {@code id} is the id of the appointment and is generated automatically<br>
 * {@code tutor} is used for referencing between tutor and appointment<br>
 * {@code student} is used for referencing between student and appointment<br>
 * {@code date} is necessary to know on which date an appointment takes place<br>
 * {@code day} of the week (Monday, Tuesday, etc.)<br>
 * {@code time} of the day (00:00-23:59:59.999999999)<br>
 * {@code availability} enumeration to difference between the tutors availability<br>
 * {@code wage} holds the wage at this date from the tutor<br>
 * 
 * @version	1.0
 *
 */
@Entity
@Table(name = "appointment", uniqueConstraints = @UniqueConstraint(columnNames = {"timestamp", "tutor"}) )
public class Appointment {
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="tutor")
	private User tutor;
	
	@ManyToOne
	@JoinColumn(name="student")
	private User student;
	
	@NotNull
	@Column(name="timestamp")
	private Timestamp timestamp;
	
	@NotNull
	@Column(name="day")
	private DayOfWeek day;
	
	@NotNull
	private Availability availability;
	
	@NumberFormat(style=Style.CURRENCY)
	private BigDecimal wage;

	/* Constructors */
	public Appointment() {
		super();
	}
	
	public Appointment(User tutor, User student, DayOfWeek day, Timestamp timestamp, Availability availability, BigDecimal wage) {
		this.tutor = tutor;
		this.student = student;
		this.day = day;
		this.timestamp = timestamp;
		this.availability = availability;
		this.wage = wage;
	}

	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getTutor() {
		return tutor;
	}

	public void setTutor(User tutor) {
		this.tutor = tutor;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Timestamp getDate() {
		return timestamp;
	}

	public void setDate(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public void setDay(DayOfWeek day) {
		this.day = day;
	}
	
	public Availability getAvailability() {
		return availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public BigDecimal getWage() {
		return wage;
	}

	public void setWage(BigDecimal wage) {
		this.wage = wage;
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
		Appointment other = (Appointment) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", tutor=" + tutor + ", student=" + student + ", timestamp=" + timestamp
				+ ", day=" + day + ", availability=" + availability + ", wage=" + wage + "]";
	}
	
}
