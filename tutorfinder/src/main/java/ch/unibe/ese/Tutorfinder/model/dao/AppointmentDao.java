package ch.unibe.ese.Tutorfinder.model.dao;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {

	Appointment findById (long id);
	
	ArrayList<Appointment> findAllById (long id);
	
	ArrayList<Appointment> findAllByUser (User user);
	
	LinkedList<Appointment> findByDay(DayOfWeek day);
}
