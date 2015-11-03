package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.time.DayOfWeek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

/**
 * Service to save the information from the {@link UpdateTimetableForm}
 * to the timetable-table on the database. This service is used for
 * update the tutors availability information.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Service
public class UpdateTimetableServiceImpl implements UpdateTimetableService{

	@Autowired	UserDao userDao;
	@Autowired	TimetableDao timetableDao;
	
	public UpdateTimetableServiceImpl() {}
	
	@Transactional
	public UpdateTimetableForm saveFrom(UpdateTimetableForm updateTimetableForm, Principal authUser)
			throws InvalidTimetableException {
		
		User user = userDao.findByEmail(authUser.getName());
		Boolean[][] tmpTimetable = updateTimetableForm.getTimetable();
		
		for(int col=0; col<=6; col++) {
			for(int row=0; row<=23; row++) {
				DayOfWeek day = DayOfWeek.of(col + 1);
				int timeslot = row;
				Timetable timetable;
				
				//if a timetable exists only change the availability, else create a new timetable
				timetable = timetableDao.findByUserAndDayAndTimeslot(user, day, timeslot);
				if(timetable != null) {
					if(tmpTimetable[row][col]) {
						timetable = timetableDao.save(timetable); //Save object to DB
					}else {
						timetableDao.delete(timetable);
					}
				} else {
					if(tmpTimetable[row][col]) {
						timetable = new Timetable(user, day, timeslot);
						timetable = timetableDao.save(timetable); //Save object to DB
					}
				}
			}
		}
		
		return updateTimetableForm;
	}
	

}
