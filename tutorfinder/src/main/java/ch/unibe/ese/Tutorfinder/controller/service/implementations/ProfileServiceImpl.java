package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;

public class ProfileServiceImpl implements ProfileService {

	@Autowired
	ProfileDao profileDao;
	
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

}
