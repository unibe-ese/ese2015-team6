package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
/**
 * @version	1.0
 */
public interface TimetableService {
	
	/**
	 * saves the timetables in a given updateTimetableForm in the database for the user
	 * corresponding to a given {@link Principal}
	 * @param updateTimetableForm containing information what timetables should be saved
	 * @param user {@link Principal}
	 * @return updateTimetableForm unchanged as given
	 * @throws InvalidTimetableException
	 */
	public UpdateTimetableForm saveFrom(UpdateTimetableForm updateTimetableForm, Principal user) throws InvalidTimetableException;

	/**
	 * finds all timetable entries in the database for a given user
	 * @param dbUser of which the timetable entries should be retrieved from the database
	 * @return list of timetables in database for given user
	 */
	public List<Timetable> findAllByUser(User dbUser);

	/**
	 * finds all timetable entries in the database for a given user and day of the week
	 * @param dbUser of which the timetable entries should be retrieved from the database
	 * @param dow Day of Week that must match the one of the timetables
	 * @return list of timetables in database for given user and day of the week
	 */
	public List<Timetable> findAllByUserAndDay(User tutor, DayOfWeek dow);

}
