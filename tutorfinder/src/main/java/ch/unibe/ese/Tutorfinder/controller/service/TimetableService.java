package ch.unibe.ese.Tutorfinder.controller.service;

import java.time.DayOfWeek;
import java.util.List;

import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

public interface TimetableService {

	List<Timetable> findAllByUser(User dbUser);

	List<Timetable> findAllByUserAndDay(User tutor, DayOfWeek dow);

}
