package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.model.Profile;

public interface ProfileService {
	
	public Profile getProfileById(Long id);
	
	public Profile getProfileByEmail(String email);

}
