package unitTest.service.implementation;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
	
	private Profile mockProfile = Mockito.mock(Profile.class);
	
	private User mockUser = Mockito.mock(User.class);
	
	@Test
	public void testSaveFrom() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(mockProfile);
		when(profileDao.save(any(Profile.class))).then(returnsFirstArg());
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		UpdateProfileForm updateProfileForm = new UpdateProfileForm();
		updateProfileForm.setFirstName("testFirstName");
		updateProfileForm.setLastName("testLastName");
		updateProfileForm.setPassword("testPassword");
		updateProfileForm.setConfirmPassword("testPassword");
		updateProfileForm.setBiography("This is a test biography");
		updateProfileForm.setRegion("Bern");
		updateProfileForm.setWage(new BigDecimal(50));
		
		UpdateProfileForm tmpUpdateProfileForm = profileService.saveFrom(updateProfileForm, mockUser);
		
		//THEN
		assertEquals("This is a test biography", tmpUpdateProfileForm.getBiography());
		assertEquals("testFirstName", tmpUpdateProfileForm.getFirstName());
	}
	
	@Test
	public void testSaveFromWithNullPassword() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(mockProfile);
		when(profileDao.save(any(Profile.class))).then(returnsFirstArg());
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		UpdateProfileForm updateProfileForm = new UpdateProfileForm();
		updateProfileForm.setFirstName("testFirstName");
		updateProfileForm.setLastName("testLastName");
		updateProfileForm.setPassword(null);
		updateProfileForm.setConfirmPassword(null);
		updateProfileForm.setBiography("This is a test biography");
		updateProfileForm.setRegion("Bern");
		updateProfileForm.setWage(new BigDecimal(50));
		
		UpdateProfileForm tmpUpdateProfileForm = profileService.saveFrom(updateProfileForm, mockUser);
		
		//THEN
		assertEquals(null, tmpUpdateProfileForm.getPassword());
	}
	
	@Test
	public void testSaveFromWithExistingPassword() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(mockProfile);
		when(profileDao.save(any(Profile.class))).then(returnsFirstArg());
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		when(mockUser.getPassword()).thenReturn("testPassword");

		
		//WHEN
		UpdateProfileForm updateProfileForm = new UpdateProfileForm();
		updateProfileForm.setFirstName("testFirstName");
		updateProfileForm.setLastName("testLastName");
		updateProfileForm.setPassword("testPassword");
		updateProfileForm.setConfirmPassword("testPassword");
		updateProfileForm.setBiography("This is a test biography");
		updateProfileForm.setRegion("Bern");
		updateProfileForm.setWage(new BigDecimal(50));
		
		UpdateProfileForm tmpUpdateProfileForm = profileService.saveFrom(updateProfileForm, this.mockUser);
		
		//THEN
		assertEquals("testPassword", tmpUpdateProfileForm.getPassword());
	}
	
	@Test
	public void testGetProfileByEmail() {
		//GIVEN
		when(profileDao.findByEmail(anyString())).thenReturn(mockProfile);
		
		//WHEN
		Profile tmpProfile = profileService.getProfileByEmail("test@test.test");
		
		//THEN
		assertEquals(tmpProfile, mockProfile);
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
		when(profileDao.findOne(anyLong())).thenReturn(mockProfile);
				
		//WHEN
		Profile tmpProfile = profileService.getProfileById(new Long(1));
				
		//THEN
		assertEquals(tmpProfile, mockProfile);
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

}
