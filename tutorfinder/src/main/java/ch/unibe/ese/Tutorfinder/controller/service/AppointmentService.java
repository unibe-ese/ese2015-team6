package ch.unibe.ese.Tutorfinder.controller.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;

public interface AppointmentService {

	MakeAppointmentsForm saveFrom(MakeAppointmentsForm appForm, Integer slot, User tutor, User student);

	Appointment findByTutorAndTimestamp(User user, Timestamp timestamp);
	
	List<AppointmentPlaceholder> findByTutorAndDate(User user, LocalDate date);

}
