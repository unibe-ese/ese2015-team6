package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

/**
 * Service to save the information from the {@code UpdateProfileForm}
 * to the profile-table on the database. This service is used for
 * update the users information.
 * 
 * @author Antonio
 *
 */
@Service
public class UpdateProfileServiceImpl implements UpdateProfileService {

	@Autowired	ProfileDao profileDao;
	@Autowired	UserDao userDao;
	
	public UpdateProfileServiceImpl() {
	}
	
	//FIXME Prohibit negative wages
	@Transactional
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, Principal user) throws InvalidUserException {
		Profile profile;
		profile = profileDao.findByEmail(user.getName());
		profile.setBiography(updateProfileForm.getBiography());
		profile.setRegion(updateProfileForm.getRegion());
		profile.setWage(updateProfileForm.getWage());
		
		profile = profileDao.save(profile); //save object to DB
		
		User tmpUser;
		tmpUser = userDao.findByEmail(user.getName());
		tmpUser.setFirstName(updateProfileForm.getFirstName());
		tmpUser.setLastName(updateProfileForm.getLastName());
		if (updateProfileForm.getPassword() != null && 
				tmpUser.getPassword() != updateProfileForm.getPassword()) {
			tmpUser.setPassword(updateProfileForm.getPassword());
		}
		
		tmpUser = userDao.save(tmpUser);	//save object to DB
		
		updateProfileForm.setId(profile.getId());
		
		
		return updateProfileForm;
	}
	
}
