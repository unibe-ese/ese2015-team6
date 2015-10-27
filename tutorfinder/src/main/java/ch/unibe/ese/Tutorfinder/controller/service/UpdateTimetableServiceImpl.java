package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

public class UpdateTimetableServiceImpl implements UpdateTimetableService{

	@Autowired	UserDao userDao;
	@Autowired	TimetableDao timetableDao;
	
	public UpdateTimetableServiceImpl() {}
	
	@Transactional
	public UpdateTimetableForm saveFrom(UpdateTimetableForm updateTimetableForm, Principal authUser)
			throws InvalidTimetableException {
		
		User user = userDao.findByEmail(authUser.getName());
		List<Timetable> tmpTimetable = timetableDao.findAllByUser(user);
		timetableDao.delete(tmpTimetable);
		List<Timetable> timetables = new ArrayList<Timetable>();
		//TODO implement timetableColum see Row
		

		
		return updateTimetableForm;
	}

}
