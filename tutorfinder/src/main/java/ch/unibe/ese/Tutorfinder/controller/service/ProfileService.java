package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * @version	1.0
 *
 */
public interface ProfileService {
	
	/**
	 * retrieves the {@link Profile} with a given id
	 * @param id of the {@link Profile} that should be returned from the database
	 * @return {@link Profile} with the given id
	 */
	public Profile getProfileById(Long id);
	
	/**
	 * retrieves the {@link Profile} with a given email
	 * @param email of the {@link Profile} that should be returned from the database
	 * @return {@link Profile} with the given email
	 */
	public Profile getProfileByEmail(String email);
	
	/**
	 * takes an {@link updateProfileForm} and saves its data into an given users profile
	 * @param updateProfileForm - contains the data that should be stored in the profile
	 * @param user whose profile should be updated
	 * @return  @link updateProfileForm} that was given as an input except with the profile id added
	 */
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, User user);

}
