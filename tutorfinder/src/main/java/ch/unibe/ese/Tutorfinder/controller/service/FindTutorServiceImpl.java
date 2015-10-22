package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.Tutorfinder.controller.pojos.FindTutorForm;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.UserSubject;
import ch.unibe.ese.Tutorfinder.model.dao.UserSubjectDao;

public class FindTutorServiceImpl implements FindTutorService {
	
	@Autowired
	UserSubjectDao userSubjectDao;
	
	
	/**
	 * Finds all Users that have passed an given subject
	 * @param subject -  String containing the name of the subject that user must have passed
	 */
	@Override
	public Iterable<User> getUsersFrom(FindTutorForm findTutorForm) {
		LinkedList<UserSubject> tmpUserSubject = userSubjectDao.findBySubject(findTutorForm.getSubject());
		
		LinkedList<User> tmpUsers = new LinkedList<User>();
		
		for(UserSubject userSubject : tmpUserSubject) {
			tmpUsers.add(userSubject.getUser());
		}
		return tmpUsers;
	}

}
