package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

public interface PrepareFormService {
	
	
	/**
	 * Injects needed objects into a given {@link ModelAndView} and gets the
	 * {@link Subjects} for the {@link UpdateSubjectsForm} from the DB
	 * essentially discarding the form's current state.
	 * 
	 * @param authUser
	 *            {@link Principal}
	 * @param model
	 *            {@link ModelAndView}
	 * @return
	 */
	public ModelAndView prepareForm(Principal authUser, ModelAndView model);
	
	/**
	 * Gets the profile which belongs to the actually logged in user
	 * 
	 * @param user
	 *            {@link Principal} is needed to get the right profile
	 * @return profile of the actually logged in user
	 */
	public Profile getUsersProfile(Principal user);
	
	//TODO comment
	public UpdateTimetableForm getUpdateTimetableFormWithValues(User dbUser);
	
	/**
	 * Converts an ArrayList of Subjects into a {@link UpdateSubjectsForm}
	 * filled with rows containing all the information from the given subjects
	 * 
	 * @param subjectList
	 *            Subject array list (from db)
	 * @return UpdateSubjectForm filled with rows
	 */
	public UpdateSubjectsForm getUpdateSubjectWithValues(ArrayList<Subject> subjectList);
	
	/**
	 * Gets an form with the users new information
	 * 
	 * @param user
	 *            {@link Principal}
	 * @return form with the users input values
	 */
	public UpdateProfileForm getFormWithValues(Principal user);
	
	

}
