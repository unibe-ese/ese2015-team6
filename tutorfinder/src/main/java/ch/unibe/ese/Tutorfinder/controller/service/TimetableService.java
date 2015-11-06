package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

public interface TimetableService {
	
	public UpdateTimetableForm saveFrom(UpdateTimetableForm updateTimetableForm, Principal user) throws InvalidTimetableException;

	List<Timetable> findAllByUser(User dbUser);

	List<Timetable> findAllByUserAndDay(User tutor, DayOfWeek dow);

}
