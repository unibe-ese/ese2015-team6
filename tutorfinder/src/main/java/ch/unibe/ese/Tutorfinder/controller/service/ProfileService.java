package ch.unibe.ese.Tutorfinder.controller.service;

import java.math.BigDecimal;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * @version	2.0
 *
 */
public interface ProfileService {
	
	/**
	 * retrieves the {@link Profile} with a given id
	 * @param id of the {@link Profile} that should be returned from the database.
	 * Cannot be negative nor null 
	 * @return {@link Profile} with the given id
	 */
	public Profile getProfileById(Long id);
	
	/**
	 * retrieves the {@link Profile} with a given email
	 * @param email of the {@link Profile} that should be returned from the database
	 * cannot be null nor empty
	 * @return {@link Profile} with the given email
	 */
	public Profile getProfileByEmail(String email);
	
	/**
	 * takes an {@link updateProfileForm} and saves its data into an given users profile
	 * @param updateProfileForm - contains the data that should be stored in the profile,
	 * cannot be null
	 * @param user whose profile should be updated, cannot be null
	 * @return  @link updateProfileForm} that was given as an input except with the profile id added
	 */
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, User user);
	
	/**
	 * Updates the {@code tutors} total rating to the given {@code rating}
	 * 
	 * @param tutor for which the rating should be updated
	 * @param rating, which should be the new one
	 * @param countedRating, is the total counted ratings for the tutor
	 */
	public void updateRating(User tutor, BigDecimal rating, BigDecimal countedRating);

}
