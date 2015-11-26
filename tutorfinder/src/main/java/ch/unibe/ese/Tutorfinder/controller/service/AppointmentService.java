package ch.unibe.ese.Tutorfinder.controller.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

/**
 * @version	1.0
 *
 */
public interface AppointmentService {

	/**
	 * saves appointment with at a given time between a given user and tutor
	 * @param appForm stores date
	 * @param slot determines at what time the appointment takes place
	 * @param tutor that is part of the appointment
	 * @param student - user that asked for the appointment
	 * @return
	 */
	public MakeAppointmentsForm saveFrom(MakeAppointmentsForm appForm, Integer slot, User tutor, User student);
	
	/**
	 * retrieves appointment of a given user at a given time
	 * @param user acts as tutor in the appointment
	 * @param timestamp at what time the appointment takes place
	 * @return appointment at given time with given tutor
	 */
	public Appointment findByTutorAndTimestamp(User user, Timestamp timestamp);
	
	/**
	 * creates and returns a list of AppointmentPlaceholders into which availability of
	 * an appointment is stored if an appointment is present for that timeslot
	 * @param user of who the appointment should be loaded in the AppointmentPlaceholders
	 * @param date for which the list should be created
	 * @return list of AppointmentPlaceholders with availability 
	 */
	public List<AppointmentPlaceholder> findByTutorAndDate(User user, LocalDate date);

	/**
	 * takes list of Timetables and creates an List of AppointmentPlaceholdes with an placeholder 
	 * for each slot in the timetables 
	 * @param slots that need a corresponding placeholder
	 * @param user of which the AppointPlaceholder should be filled with appointment data if present
	 * @param date 
	 * @return list of AppointmentPlaceholders that contains a AppointmentPlaceholer with matching time
	 * to a given timetable slot
	 */
	public List<AppointmentPlaceholder> loadAppointments(List<Timetable> slots, User user, LocalDate date);
	
	/**
	 * Updates the {@link Availability} of the {@link Appointment} with the 
	 * id {@code appointmentId} to the new {@code availability}.
	 * 
	 * @param availability the new availability for the appointment
	 * @param appointmentId the id for the appointment which should be updated
	 */
	public Appointment updateAppointment(Availability availability, Long appointmentId);
	
	/**
	 * Searches all {@link Availability} {@link Appointment}s of an {@code Tutor} and
	 * returns only the past appointments.
	 * 
	 * @param tutor for which the past {@code availability} appointments are searched
	 * @param availability which the searched appointments have
	 * @return
	 */
	public List<Appointment> getPastAppointments(User tutor, Availability availability);
	
	/**
	 * Searches all {@link Availability} {@link Appointment}s of an {@code Tutor} and
	 * returns only the appointments which are not in the past.
	 * 
	 * @param tutor for which the {@code availability} appointments in the future are searched
	 * @param availability which the searched appointments have
	 * @return
	 */
	public List<Appointment> getFutureAppointments(User tutor, Availability availability);

	/**
	 * Searches all {@code Reserved} {@link Appointment}s of an {@code Student} and
	 * returns only the appointments which are not in the future.
	 * 
	 * @param student for which the reserved appointments in the future are searched
	 * @return A list of appointments with the {@link Availability} {@code Reserved}
	 */
	public List<Appointment> getPendingAppointments(User student);
	
	/**
	 * Searches all {@code Arranged} {@link Appointment}s of an {@code Student} and
	 * returns only the appointments which are not in the past.
	 * 
	 * @param student for which the arranged appointments in the past are searched
	 * @return A list of appointments with the {@link Availability} {@code Arranged}
	 */
	public List<Appointment> getFutureAppointmentsAsStudent(User student);
	
	/**
	 * Searches all {@code Arranged} {@link Appointment}s of an {@code Student} and
	 * returns only the appointments which are in the past.
	 * 
	 * @param student for which the arranged appointments in the past are searched
	 * @return A list of appointments with the {@link Availability} {@code Arranged}
	 */
	public List<Appointment> getPastAppointmentsAsStudent(User student);
	
	/**
	 * Updates first the {@link Appointment} with the given {@code appointmentId},
	 * afterwards it updates the tutors total rating.
	 * 
	 * @param appointmentId
	 * @param rating
	 */
	public void rateTutorForAppointment(Long appointmentId, BigDecimal rating);

}
