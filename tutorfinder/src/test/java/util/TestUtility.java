package util;

import java.security.Principal;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

/**
 * Utility for Spring integration testing. Provides static methods for creating
 * test users and accessing their values. 
 *
 */
public class TestUtility {
	public static final String FIRST_USER_USERNAME = "user@provider.tld";
	public static final String SECOND_USER_USERNAME = "userTwo@provider.tld";
	public static final String THIRD_USER_USERNAME = "userThree@provider.tld";

	public static User testUser;
	public static User testUserTwo;
	public static User testUserThree;
	public static Profile testProfile;
	public static Profile testProfileTwo;
	public static Profile testProfileThree;

	/**
	 * Initializes all test users so they can be used in testing. Must be called before {@code setUp}
	 */
	public static void initialize() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		/* Test user number one */
		testUser = new User();
		testUser.setEmail(FIRST_USER_USERNAME);
		testUser.setPassword(encoder.encode("password"));
		testUser.setFirstName("FirstName");
		testUser.setLastName("LastName");
		testUser.setRole(ConstantVariables.TUTOR);
		testProfile = new Profile(testUser.getEmail());
		testProfile.setWage(ConstantVariables.MIN_WAGE);

		/* Test user number two */
		testUserTwo = new User();
		testUserTwo.setEmail(SECOND_USER_USERNAME);
		testUserTwo.setPassword(encoder.encode("password"));
		testUserTwo.setFirstName("FirstNameTwo");
		testUserTwo.setLastName("LastNameTwo");
		testUserTwo.setRole(ConstantVariables.TUTOR);
		testProfileTwo = new Profile(testUserTwo.getEmail());
		testProfileTwo.setWage(ConstantVariables.MIN_WAGE);

		/* Test user number three */
		testUserThree = new User();
		testUserThree.setEmail(THIRD_USER_USERNAME);
		testUserThree.setPassword(encoder.encode("password"));
		testUserThree.setFirstName("FirstNameThree");
		testUserThree.setLastName("LastNameThree");
		testUserThree.setRole(ConstantVariables.STUDENT);
		testProfileThree = new Profile(testUserThree.getEmail());
		testProfileThree.setWage(ConstantVariables.MIN_WAGE);
	}
	
	/**
	 * Saves test users to given Dao's. {@code initialize} has to be called first for this to work 
	 * @param userDao
	 * @param profileDao
	 */
	public static void setUp(UserDao userDao, ProfileDao profileDao) {
		assert testUser != null : "Please call initialize() before calling setUp!";
			
		/* Test user number one */
		testProfile = profileDao.save(testProfile);
		testUser.setProfile(testProfile);
		testUser = userDao.save(testUser);

		/* Test user number two */
		testProfileTwo = profileDao.save(testProfileTwo);
		testUserTwo.setProfile(testProfileTwo);
		testUserTwo = userDao.save(testUserTwo);

		/* Test user number three */
		testProfileThree = profileDao.save(testProfileThree);
		testUserThree.setProfile(testProfileThree);
		testUserThree = userDao.save(testUserThree);
	}

	/**
	 * Creates a test principal to be used in tests which require a specific principal
	 * @param username
	 * @param password
	 * @param authority
	 * @return
	 */
	public static Principal createPrincipal(String username, String password, String authority) {
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authority);
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);
		return authentication;
	}

}
