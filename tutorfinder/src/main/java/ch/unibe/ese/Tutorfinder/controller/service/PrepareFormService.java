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

/**
 * 
 * @author  Antonio
 * @author	Florian
 * @author	Lukas
 * @author	Nicola
 * @version	1.0
 *
 */
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
	 * Returns the profile corresponding to a {@code Principal} user.
	 * 
	 * @param user
	 *            {@link Principal} is needed to get the right profile
	 * @return profile obj. that belongs to the Principal
	 */
	public Profile getUsersProfile(Principal user);
	
	public UpdateTimetableForm getUpdateTimetableFormWithValues(User dbUser);
	
	/**
	 * Converts an ArrayList of Subjects into a {@link UpdateSubjectsForm}
	 * filled with rows containing all the information from the given subjects
	 * (corresponding name and grade)
	 * 
	 * @param subjectList
	 *            Arraylist of Subjects that should be added to the form
	 * @return UpdateSubjectForm containing one or multiple row obj. with subject name and grade
	 */
	public UpdateSubjectsForm getUpdateSubjectWithValues(ArrayList<Subject> subjectList);
	
	/**	
	 * reads data that belongs to the currently logged in user (Principal) from
	 * the database and writes it into an UpdateProfileForm
	 * 
	 * @param authUser the {@link Principal} user, which is actual logged in
	 * @return UpdateProfileForm filled with profile info from the database
	 */
	public UpdateProfileForm getFormWithValues(Principal authUser);

	/**
	 * Prepares an model to display an users {@link Profile} with {@link Subject}'s and the possibility 
	 * to reserve an appointment by the {@link Principal} user.
	 * 
	 * @param authUser {@link Principal} actual logged in user
	 * @param userId Parameter which is needed to get the right {@link Profile} and the right URL
	 * @param model {@link ModelAndview} which needs to be updated
	 * @return updated {@link ModelAndview} with the {@link Profile} with the {@link User} Information and {@link Subject}'s
	 * 					and the actual logged in {@link Principal} user
	 */
	public ModelAndView prepareModelByUserId(Principal authUser, long userId, ModelAndView model);
	
	

}
