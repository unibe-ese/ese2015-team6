package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.LinkedList;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

public interface FindTutorService {
	
	public Iterable<User> getUsersFrom(FindTutorForm findTutorForm);
	
	public LinkedList<Subject> getSubjectsFrom(String query);

}
