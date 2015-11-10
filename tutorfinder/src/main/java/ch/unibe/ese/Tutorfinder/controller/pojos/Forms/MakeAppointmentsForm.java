package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
/**
 * Form to get to save temporarily the basic {@link Timetable} of 
 * an {@link User} and the reserved or arranged {@link Appointments}.
 * Is needed to get the actual status of all possible {@code Timeslots} 
 * from and user in the {@code showProfile.html} view.
 * 
 * @version	1.0
 *
 */
public class MakeAppointmentsForm {
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	
	private List<AppointmentPlaceholder> appointments;
	
	/* Getters and Setters */
	public List<AppointmentPlaceholder> getAppointments() {
		return appointments;
	}
	
	public void setAppointments(List<AppointmentPlaceholder> appointmentList) {
		this.appointments = appointmentList;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
