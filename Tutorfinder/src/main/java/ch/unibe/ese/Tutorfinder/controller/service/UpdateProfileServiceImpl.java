package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.Profile;

@Service
public class UpdateProfileServiceImpl implements UpdateProfileService {

	@Autowired	ProfileDao profileDao;
	
	public UpdateProfileServiceImpl() {
	}
	
	@Transactional
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, Principal user) throws InvalidUserException {
		Profile profile;
		System.out.println("HALLO" + user.getName());
		profile = profileDao.findByEmail(user.getName());//TODO getId doesn't work correct
		profile.setBiography(updateProfileForm.getBiography());
		//profile.setRegion(updateProfileForm.getRegion());
		//profile.setWage(updateProfileForm.getWage());
		//profile.setImgPath(updateProfileForm.getImgPath());
		
		profile = profileDao.save(profile); //save object to DB
		
		updateProfileForm.setId(profile.getId());
		
		
		return updateProfileForm;
	}
	
}
