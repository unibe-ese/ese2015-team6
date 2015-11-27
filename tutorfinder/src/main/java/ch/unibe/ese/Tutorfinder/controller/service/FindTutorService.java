package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.List;
import java.util.Map;

import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Service to find tutors with the selected subject.
 * @version	1.0
 *
 */
public interface FindTutorService {
	
	/**
	 * finds all subjects whose names contain a given query
	 * @param query - String that should be contained in the subjects name
	 */
	public Map<User, List<Subject>> getSubjectsFrom(String query);

}
