package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidProfileException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;

public interface ProfileService {
	
	public Profile getProfileById(Long id);
	
	public Profile getProfileByEmail(String email);
	
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, User user) throws InvalidProfileException;

}
