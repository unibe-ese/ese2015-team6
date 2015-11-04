package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.time.LocalDate;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;

public class MakeAppointmentsForm {
	
	private LocalDate date;
	
	private List<AppointmentPlaceholder> appointmentList;
	
	public List<AppointmentPlaceholder> getAppointmentList() {
		return appointmentList;
	}
	public void setAppointmentList(List<AppointmentPlaceholder> appointmentList) {
		this.appointmentList = appointmentList;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
