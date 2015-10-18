package ch.unibe.ese.Tutorfinder.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Appointment;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {

}
