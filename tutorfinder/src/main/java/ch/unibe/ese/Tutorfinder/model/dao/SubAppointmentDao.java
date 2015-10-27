package ch.unibe.ese.Tutorfinder.model.dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.SubAppointment;

public interface SubAppointmentDao extends CrudRepository<SubAppointment, Long>{

	SubAppointment findById (long id);
	
	ArrayList<SubAppointment> findAllById (long id);
	
	ArrayList<SubAppointment> findAllByAppointment (Appointment appointment);
	
	LinkedList<SubAppointment> findByTime(LocalTime time);
}
