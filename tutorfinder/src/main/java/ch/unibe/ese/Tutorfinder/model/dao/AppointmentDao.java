package ch.unibe.ese.Tutorfinder.model.dao;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {
	
	ArrayList<Appointment> findAllByTutor (User tutor);
	
	ArrayList<Appointment> findAllByStudent (User student);
	
	Appointment findByTutorAndTimestamp (User tutor, Timestamp timestamp);
	
	ArrayList<Appointment> findAllByTutorAndAvailabilityOrderByTimestampDesc (User tutor, Availability availability);
	
	ArrayList<Appointment> findAllByStudentAndAvailabilityOrderByTimestampDesc (User student, Availability availability);
	
	ArrayList<Appointment> findAllByTutorAndAvailabilityOrderByTimestampAsc (User tutor, Availability availability);
	
	ArrayList<Appointment> findAllByStudentAndAvailabilityOrderByTimestampAsc (User student, Availability availability);
}
