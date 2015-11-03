package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.util.Date;
import java.util.List;

import ch.unibe.ese.Tutorfinder.model.Appointment;

public class MakeAppointmentsForm {
	
	private Date date;
	
	private List<Appointment> appointmentList;
	
	public List<Appointment> getAppointmentList() {
		return appointmentList;
	}
	public void setAppointmentList(List<Appointment> appointmentList) {
		this.appointmentList = appointmentList;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
