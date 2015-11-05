package ch.unibe.ese.Tutorfinder.controller.service;

import java.sql.Timestamp;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;

public interface AppointmentService {

	void saveFrom(MakeAppointmentsForm appForm, Integer slot, User tutor, User student);

	Appointment findByTutorAndTimestamp(User user, Timestamp timestamp);

}
