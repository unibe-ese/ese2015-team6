package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.TimetableRow;
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
 * @author Antonio
 *
 */
@Service
public class UpdateTimetableServiceImpl implements UpdateTimetableService{

	@Autowired	UserDao userDao;
	@Autowired	TimetableDao timetableDao;
	
	public UpdateTimetableServiceImpl() {}
	
	/**
	 * Replaces all currently saved timetables for one tutor with the timetables
	 * currently in the form by deleting and re-adding them
	 */
	@Transactional
	public UpdateTimetableForm saveFrom(UpdateTimetableForm updateTimetableForm, Principal authUser)
			throws InvalidTimetableException {
		
		User user = userDao.findByEmail(authUser.getName());
		List<Timetable> tmpTimetableList = timetableDao.findAllByUser(user);
		timetableDao.delete(tmpTimetableList);
		List<Timetable> timetables = new ArrayList<Timetable>();
		List<TimetableRow> timetableRowList = updateTimetableForm.getTimetableRows();
		
		for (TimetableRow timetableRow : timetableRowList) {
			Timetable timetable = new Timetable();
			timetable.setUser(user);
			timetable.setDay(timetableRow.getDay());
			timetable.setTime(timetableRow.getTime());
			timetable.setAvailability(timetableRow.getAvailability());
			timetables.add(timetable);
		}
		try {
			timetableDao.save(timetables);
		} catch (DataIntegrityViolationException e) {
			timetableDao.save(tmpTimetableList);
			//TODO Inject some error message into form or similar
		}		
		updateTimetableForm.setId(user.getId());
		return updateTimetableForm;
	}

}
