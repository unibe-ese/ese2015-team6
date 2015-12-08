package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.ArrayList;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * @version	2.0
 */
public interface SubjectService {
	
	/**
	 * takes an {@link User} and returns all subjects of the given user
	 * @param user of which the subject should be returned
	 * @return ArrayList of {@link Subject} that belong to the given user
	 */
	public ArrayList<Subject> getAllSubjectsByUser(User user);
	
	/**
	 * takes a {@link updateSubjectsForm}, parses the subjects and saves them in
	 * the database
	 * @param updateSubjectsForm contains subject names and grades
	 * @param user {@link Principal} to that the subjects belong to
	 * @return {@link updateSubjectsForm} that was given as an input with user id added
	 * @throws InvalidSubjectException is thrown when a the updateSubjectsForm contains two
	 * entries with the same subject name
	 */
	public UpdateSubjectsForm saveFrom(UpdateSubjectsForm updateSubjectsForm, Principal user) throws InvalidSubjectException;
	
	/**
	 * Calculates the average value for all subjects of a given user
	 * @param {@link User} to calculate average from
	 * @return average as double
	 */
	public double getAverageGradeByUser(User user);

}
