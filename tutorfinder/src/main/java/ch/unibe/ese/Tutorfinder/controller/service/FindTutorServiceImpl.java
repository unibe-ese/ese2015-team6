package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.pojos.FindTutorForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;

@Service
public class FindTutorServiceImpl implements FindTutorService {
	
	@Autowired
	SubjectDao subjectDao;
	
	
	/**
	 * Finds all Users that have passed an given subject
	 * @param findTutorForm - Form that lets user enter a subject 
	 */
	@Override
	public Iterable<User> getUsersFrom(FindTutorForm findTutorForm) {
		LinkedList<Subject> tmpUserSubject = subjectDao.findByName(findTutorForm.getSubject());
		
		LinkedList<User> tmpUsers = new LinkedList<User>();
		
		for(Subject userSubject : tmpUserSubject) {
			tmpUsers.add(userSubject.getUser());
		}
		return tmpUsers;
	}

}
