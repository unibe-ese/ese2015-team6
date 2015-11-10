package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

/**
 * Class for validating the users input in the {@code showProfile.html}.
 * 
 * @version	1.0
 *
 */
public class UpdateAppointmentsForm {

	@NotNull
	private long id;
	
	@NotNull
	private User tutor;
	
	private User student;
	
	private LocalDate date;
	
	@NotNull
	private DayOfWeek day;
	
	@NotNull
	private LocalTime time;
	
	@NotNull
	private Availability availability;
	
	@NotNull
	private BigDecimal wage;

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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
	
	
}
