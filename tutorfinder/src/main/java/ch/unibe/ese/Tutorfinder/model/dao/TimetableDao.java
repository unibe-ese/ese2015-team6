package ch.unibe.ese.Tutorfinder.model.dao;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

public interface TimetableDao extends CrudRepository<Timetable, Long> {

	Timetable findById (long id);
	
	ArrayList<Timetable> findAllById (long id);
	
	ArrayList<Timetable> findAllByUser (User user);
	
	LinkedList<Timetable> findByDay(DayOfWeek day);
}
