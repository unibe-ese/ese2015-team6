package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;

public class TimetableServiceImpl implements TimetableService {

	@Autowired TimetableDao timetableDao;
	
	@Override
	public List<Timetable> findAllByUser(User user) {
		assert (user != null);
		List<Timetable> returnList = timetableDao.findAllByUser(user);
		assert (returnList != null);
		return returnList;
	}

}
