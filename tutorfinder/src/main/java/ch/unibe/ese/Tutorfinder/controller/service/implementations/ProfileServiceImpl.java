package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileDao profileDao;

	public ProfileServiceImpl() {

	}

	@Override
	public Profile getProfileById(Long id) {
		assert (id != null);
		assert (id > 0);

		Profile tmpProfile = profileDao.findOne(id);
		assert (tmpProfile != null);

		return tmpProfile;
	}

	@Override
	public Profile getProfileByEmail(String email) {
		assert (email != null);
		assert (!email.isEmpty());

		Profile tmpProfile = profileDao.findByEmail(email);
		assert (tmpProfile != null);

		return tmpProfile;
	}

	@Transactional
	public UpdateProfileForm saveFrom(UpdateProfileForm updateProfileForm, User user) {

		assert (updateProfileForm != null);
		assert (user != null);

		updateMainInformation(updateProfileForm, user);
		Profile profile = updateUserProfileInformation(updateProfileForm, user);
		updateProfileForm.setId(profile.getId());
		return updateProfileForm;
	}

	/**
	 * updates the profile of a given {@link User}
	 * 
	 * @param updateProfileForm
	 *            that contains the new profile information
	 * @param user
	 *            of which the profile information should be updated cannot be
	 *            null
	 * @return updated profile
	 */
	private Profile updateUserProfileInformation(UpdateProfileForm updateProfileForm, User user) {

		assert (user != null);
		assert (updateProfileForm != null);

		Profile profile = profileDao.findByEmail(user.getEmail());
		profile.setBiography(updateProfileForm.getBiography());
		profile.setRegion(updateProfileForm.getRegion());
		profile.setWage(updateProfileForm.getWage());
		profile.setLanguage(updateProfileForm.getLanguage());
		profile.setUniversity(updateProfileForm.getUniversity());
		
		profile = profileDao.save(profile); // save object to DB

		return profile;
	}

	/**
	 * Updates the data of a given {@link User}
	 * 
	 * @param updateProfileForm
	 *            that contains the new user data
	 * @param user
	 *            of which the data should be updated
	 */
	private void updateMainInformation(UpdateProfileForm updateProfileForm, User user) {

		assert (user != null);
		assert (updateProfileForm != null);

		user.setFirstName(updateProfileForm.getFirstName());
		user.setLastName(updateProfileForm.getLastName());
		if (updateProfileForm.getPassword() != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(updateProfileForm.getPassword()));
		}

		userService.save(user); // save object to DB

	}

	@Override
	public void updateRating(User tutor, BigDecimal rating, BigDecimal countedRating) {
		assert (tutor != null);
		assert (rating != null);
		assert (countedRating != null && countedRating != BigDecimal.ZERO);

		Profile tmpProfile = tutor.getProfile();
		tmpProfile.setRating(rating);

		Long countingRates = countedRating.longValue();
		tmpProfile.setCountedRatings(countingRates);

		profileDao.save(tmpProfile);
	}

}
