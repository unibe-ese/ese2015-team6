package ch.unibe.ese.Tutorfinder.model.dao;

import java.time.DayOfWeek;
import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

public interface TimetableDao extends CrudRepository<Timetable, Long> {

	Timetable findByUserAndDayAndTimeslot (User user, DayOfWeek day, int timeslot);
	
	LinkedList<Timetable> findAllByUser (User user);
}
