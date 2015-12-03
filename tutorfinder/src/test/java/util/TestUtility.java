package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

public class TestUtility {
	public static User testUser;
	public static Profile testProfile;
	
	public static void initialize() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		testUser = new User();
		testUser.setEmail("user@provider.tld");
		testUser.setPassword(encoder.encode("password"));
		testUser.setFirstName("FirstName");
		testUser.setLastName("LastName");
		testUser.setRole(ConstantVariables.TUTOR);
		testProfile = new Profile(testUser.getEmail());
		testProfile.setWage(ConstantVariables.MIN_WAGE);
	}
	
	public static void setUp(UserDao userDao, ProfileDao profileDao) {
		testProfile = profileDao.save(testProfile);
		testUser.setProfile(testProfile);
		testUser = userDao.save(testUser);
	}
	
}
