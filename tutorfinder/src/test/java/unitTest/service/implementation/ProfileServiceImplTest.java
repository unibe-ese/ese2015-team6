package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class ProfileServiceImplTest {
	
	@Autowired
	private ProfileService profileService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfileDao profileDao;
	
	@Mock
	private Profile mockProfile;
	@Mock
	private User mockUser;
	private UpdateProfileForm updateProfileForm;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
		
		this.updateProfileForm = new UpdateProfileForm();
		this.updateProfileForm.setFirstName("testFirstName");
		this.updateProfileForm.setLastName("testLastName");
		this.updateProfileForm.setPassword("testPassword");
		this.updateProfileForm.setConfirmPassword("testPassword");
		this.updateProfileForm.setBiography("This is a test biography");
		this.updateProfileForm.setRegion("Bern");
		this.updateProfileForm.setWage(new BigDecimal(50));
	}
	
	@Test
	public void testSaveFrom() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(this.mockProfile);
		when(profileDao.save(any(Profile.class))).then(returnsFirstArg());
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		when(mockUser.getPassword()).thenReturn("testPassword");

		//WHEN
		UpdateProfileForm tmpUpdateProfileForm = profileService.saveFrom(this.updateProfileForm, mockUser);
		
		//THEN
		assertEquals("This is a test biography", tmpUpdateProfileForm.getBiography());
		assertEquals("testFirstName", tmpUpdateProfileForm.getFirstName());
	}
	
	@Test
	public void testSaveFromWithNullPassword() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(this.mockProfile);
		when(profileDao.save(any(Profile.class))).then(returnsFirstArg());
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		this.updateProfileForm.setPassword(null);
		this.updateProfileForm.setConfirmPassword(null);

		UpdateProfileForm tmpUpdateProfileForm = profileService.saveFrom(this.updateProfileForm, mockUser);
		
		//THEN
		assertEquals(null, tmpUpdateProfileForm.getPassword());
	}
	
	@Test
	public void testSaveFromWithExistingPassword() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(this.mockProfile);
		when(profileDao.save(any(Profile.class))).then(returnsFirstArg());
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		when(mockUser.getPassword()).thenReturn("testPassword");
		
		//WHEN		
		UpdateProfileForm tmpUpdateProfileForm = profileService.saveFrom(this.updateProfileForm, this.mockUser);
		
		//THEN
		assertEquals("testPassword", tmpUpdateProfileForm.getPassword());
	}
	
	@Test
	public void testGetProfileByEmail() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(this.mockProfile);
		
		//WHEN
		Profile tmpProfile = profileService.getProfileByEmail("test@test.test");
		
		//THEN
		assertEquals(tmpProfile, this.mockProfile);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetProfileByEmailNull() {
		profileService.getProfileByEmail(null);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testGetNullProfileByEmail() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(null);
		
		//WHEN
		profileService.getProfileByEmail("test@test.test");
	}
	
	@Test
	public void testGetProfileById() {
		//GIVEN
		when(profileDao.findOne(anyLong())).thenReturn(this.mockProfile);
				
		//WHEN
		Profile tmpProfile = profileService.getProfileById(new Long(1));
				
		//THEN
		assertEquals(tmpProfile, this.mockProfile);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetProfileByIdNull() {
		profileService.getProfileById(null);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetNullProfileById() {
		//GIVEN
		when(profileDao.findOne(anyLong())).thenReturn(null);
				
		//WHEN
		profileService.getProfileById(new Long(1));
	}
	
	@Test
	public void testUpdateRating() {
		BigDecimal testRating = new BigDecimal(4.5);
		BigDecimal countedRatings = BigDecimal.ONE;
		when(mockUser.getProfile()).thenReturn(mockProfile);
		when(profileDao.save(mockProfile)).thenReturn(mockProfile);
		
		
		profileService.updateRating(mockUser, testRating, countedRatings);
		
		verify(mockProfile).setRating(testRating);
		verify(mockProfile).setCountedRatings(countedRatings.longValue());
	}
	
	@Test(expected=AssertionError.class)
	public void testUpdateRatingWhenRatingNull() {
		profileService.updateRating(mockUser, null, BigDecimal.ONE);
	}
	
	@Test(expected=AssertionError.class)
	public void testUpdateRatingWhenTutorNull() {
		profileService.updateRating(null, new BigDecimal(4.5), BigDecimal.ONE);
	}
	
	@Test(expected=AssertionError.class)
	public void testUpdateRatingWhenCountedRatingNull() {
		profileService.updateRating(mockUser, new BigDecimal(4.5), null);
	}
	
	@Test(expected=AssertionError.class)
	public void testUpdateRatingWhenCountedRatingZero() {
		profileService.updateRating(mockUser, new BigDecimal(4.5), BigDecimal.ZERO);
	}

}
