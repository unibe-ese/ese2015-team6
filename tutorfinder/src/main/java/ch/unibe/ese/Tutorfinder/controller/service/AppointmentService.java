package ch.unibe.ese.Tutorfinder.controller.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

public interface AppointmentService {

	public MakeAppointmentsForm saveFrom(MakeAppointmentsForm appForm, Integer slot, User tutor, User student);

	public Appointment findByTutorAndTimestamp(User user, Timestamp timestamp);

	public List<AppointmentPlaceholder> findByTutorAndDate(User user, LocalDate date);

	public List<AppointmentPlaceholder> loadAppointments(List<Timetable> slots, User user, LocalDate date);

}
