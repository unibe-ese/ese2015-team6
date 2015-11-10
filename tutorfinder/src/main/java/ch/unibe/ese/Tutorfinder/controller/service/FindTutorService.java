package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.LinkedList;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Service to find tutors with the selected subject.
 * 
 * @author  Antonio
 * @author	Florian
 * @author	Lukas
 * @author	Nicola
 * @version	1.0
 *
 */
public interface FindTutorService {
	
	/**
	 * Finds all Users that have passed an given subject
	 * @param findTutorForm - Form that lets user enter a subject 
	 */
	public Iterable<User> getUsersFrom(FindTutorForm findTutorForm);
	
	/**
	 * finds all subjects whose names contain a given query
	 * @param query - String that should be contained in the subjects name
	 */
	public LinkedList<Subject> getSubjectsFrom(String query);

}
