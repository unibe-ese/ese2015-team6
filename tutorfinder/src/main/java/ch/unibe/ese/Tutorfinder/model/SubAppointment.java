package ch.unibe.ese.Tutorfinder.model;

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

import ch.unibe.ese.Tutorfinder.util.Availability;

/**
 * Entity for appointment, holding following values:<br>
 * {@code id} is the id of the appointment and is generated automatically<br>
 * {@code appointment} is used for referencing between appointment and subappointment (same for id)<br>
 * {@code time} of the day  (00:00 - 23:59:59.999999999)<br>
 * {@code availability} of the user at this day and time<br>
 * 
 * @author Antonio
 *
 */
@Entity
@Table(name = "subappointment", uniqueConstraints = @UniqueConstraint(columnNames = {"time", "appointment"}) )
public class SubAppointment {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="appointment")
	private Appointment appointment;
	
	@NotNull
	@Column(name="time")
	private LocalTime time;
	
	@NotNull
	private Availability availability;
	
	/* Getters and Setters */
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
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
	
}
