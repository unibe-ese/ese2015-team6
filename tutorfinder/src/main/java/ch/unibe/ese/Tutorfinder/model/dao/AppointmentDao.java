package ch.unibe.ese.Tutorfinder.model.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {
	
	Appointment findById (long id);
	
	ArrayList<Appointment> findAllByTutor (User tutor);
	
	ArrayList<Appointment> findAllByStudent (User student);
	
	LinkedList<Appointment> findAllByDate (LocalDate date);
	

}
