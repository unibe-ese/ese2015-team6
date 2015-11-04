package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.time.LocalDate;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;

public class MakeAppointmentsForm {
	
	private LocalDate date;
	
	private List<AppointmentPlaceholder> appointments;
	
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
