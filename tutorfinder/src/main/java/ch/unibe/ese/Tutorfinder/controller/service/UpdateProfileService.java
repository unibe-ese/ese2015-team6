package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidProfileException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateProfileForm;

public interface UpdateProfileService {

	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, Principal user) throws InvalidProfileException;
}
