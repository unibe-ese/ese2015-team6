package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	SubjectDao subjectDao;
	
	@Override
	public ArrayList<Subject> getAllSubjectsByUser(User user) {
		assert(user != null);
		
		ArrayList<Subject> tmpSubjects = subjectDao.findAllByUser(user);
		assert(tmpSubjects != null);
		
		return tmpSubjects;
	}

}
