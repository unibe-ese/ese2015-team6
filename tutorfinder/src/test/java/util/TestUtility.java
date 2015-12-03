package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

public class TestUtility {
	public static User testUser;
	public static User testUserTwo;
	public static Profile testProfile;
	public static Profile testProfileTwo;
	
	public static void initialize() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		/* Test user number one */
		testUser = new User();
		testUser.setEmail("user@provider.tld");
		testUser.setPassword(encoder.encode("password"));
		testUser.setFirstName("FirstName");
		testUser.setLastName("LastName");
		testUser.setRole(ConstantVariables.TUTOR);
		testProfile = new Profile(testUser.getEmail());
		testProfile.setWage(ConstantVariables.MIN_WAGE);
		
		/* Test user number two */
		testUserTwo = new User();
		testUserTwo.setEmail("userTwo@provider.tld");
		testUserTwo.setPassword(encoder.encode("password"));
		testUserTwo.setFirstName("FirstNameTwo");
		testUserTwo.setLastName("LastNameTwo");
		testUserTwo.setRole(ConstantVariables.TUTOR);
		testProfileTwo = new Profile(testUserTwo.getEmail());
		testProfileTwo.setWage(ConstantVariables.MIN_WAGE);
	}
	
	public static void setUp(UserDao userDao, ProfileDao profileDao) {
		/* Test user number one */
		testProfile = profileDao.save(testProfile);
		testUser.setProfile(testProfile);
		testUser = userDao.save(testUser);
		
		/* Test user number two */
		testProfileTwo = profileDao.save(testProfileTwo);
		testUserTwo.setProfile(testProfileTwo);
		testUserTwo = userDao.save(testUserTwo);
	}
	
}
