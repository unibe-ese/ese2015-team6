package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Service to find tutors with the selected subject.
 * @version	1.1
 *
 */
public interface FindTutorService {
	
	/**
	 * finds all subjects whose names contain a given query
	 * @param query - String that should be contained in the subjects name
	 */
	public Map<User, List<Subject>> getSubjectsFrom(String query);

	/**
	 * Generates the comparator field from the filters specified in a {@link FindTutorFilterForm}
	 */
	public void generateComparatorFrom(FindTutorFilterForm form);
	
	/**
	 * Gets a {@link Map} sorted by the {@link Comparator} in the classes field.
	 * Failing to set the correct comparator before calling this method results in undefined behavior.
	 * 
	 * @param query
	 * @return Sorted {@link Map} of class TreeMap
	 */
	public Map<User, List<Subject>> getSubjectsSorted(String query);
}
