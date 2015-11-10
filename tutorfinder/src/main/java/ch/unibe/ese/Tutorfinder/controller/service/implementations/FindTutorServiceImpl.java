package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;

@Service
public class FindTutorServiceImpl implements FindTutorService {
	
	@Autowired
	SubjectDao subjectDao;
	
	@Override
	public Iterable<User> getUsersFrom(FindTutorForm findTutorForm) {
		LinkedList<Subject> tmpUserSubject = subjectDao.findByName(findTutorForm.getSubject());
		
		LinkedList<User> tmpUsers = new LinkedList<User>();
		
		for(Subject userSubject : tmpUserSubject) {
			if(userSubject.getUser() != null)
				tmpUsers.add(userSubject.getUser());
		}
		return tmpUsers;
	}

	@Override
	public LinkedList<Subject> getSubjectsFrom(String query) {
		LinkedList<Subject> returnList = new LinkedList<Subject>();
		Iterable<Subject> subjectIterable = subjectDao.findAll();
		for(Subject subject: subjectIterable) {
			if (subject != null) {
				if(subject.getName().toLowerCase().contains(query.toLowerCase()))
					returnList.add(subject);
			}
		}
		return returnList;
	}

}
