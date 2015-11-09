package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidProfileException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	UserService userService;
	
	@Autowired
	ProfileDao profileDao;
	
	public ProfileServiceImpl() {
		
	}
	
	@Override
	public Profile getProfileById(Long id) {
		assert(id != null);
		
		Profile tmpProfile = profileDao.findOne(id);
		assert(tmpProfile != null);
		
		return tmpProfile;
	}

	@Override
	public Profile getProfileByEmail(String email) {
		assert(email != null);
		
		Profile tmpProfile = profileDao.findByEmail(email);
		assert(tmpProfile != null);
		
		return tmpProfile;
	}
	
	@Transactional
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, User user) 
			throws InvalidProfileException {
		
		//Updates the users main information
		user.setFirstName(updateProfileForm.getFirstName());
		user.setLastName(updateProfileForm.getLastName());
		if (updateProfileForm.getPassword() != null && 
				user.getPassword() != updateProfileForm.getPassword()) {
			user.setPassword(updateProfileForm.getPassword());
		}
		
		user = userService.save(user);	//save object to DB
		
		//Updates the users profile information
		Profile profile;
		profile = profileDao.findByEmail(user.getEmail());
		profile.setBiography(updateProfileForm.getBiography());
		profile.setRegion(updateProfileForm.getRegion());
		profile.setWage(updateProfileForm.getWage());
		
		profile = profileDao.save(profile); //save object to DB
		
		
		
		
		updateProfileForm.setId(profile.getId());
		
		return updateProfileForm;
	}

}
