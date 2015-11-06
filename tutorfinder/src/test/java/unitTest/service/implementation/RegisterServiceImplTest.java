package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;
import ch.unibe.ese.Tutorfinder.controller.service.RegisterService;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class RegisterServiceImplTest {
	
	@Autowired 
	private RegisterService registerService; 
	@Autowired 
	private UserDao userDao;   
	
	 
	@Test
	public void testSaveFormForTutor() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(null);
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		SignupForm signupForm = new SignupForm();
		signupForm.setEmail("form@test.ch");
		signupForm.setFirstName("testFirstName");
		signupForm.setLastName("testLastName");
		signupForm.setPassword("testPassword");
		signupForm.setConfirmPassword("testPassword");
		signupForm.setTutor(true);
		
		SignupForm tmpSignupForm = registerService.saveFrom(signupForm);
		
		//THEN
		assertEquals("testFirstName", tmpSignupForm.getFirstName());
 
	}
	 
	@Test
	public void testSaveFormForStudent() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(null);
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		SignupForm signupForm = new SignupForm();
		signupForm.setEmail("form@test.ch");
		signupForm.setFirstName("testFirstName");
		signupForm.setLastName("testLastName");
		signupForm.setPassword("testPassword");
		signupForm.setConfirmPassword("testPassword");
		signupForm.setTutor(false);
		
		SignupForm tmpSignupForm = registerService.saveFrom(signupForm);
		
		//THEN
		assertEquals("testFirstName", tmpSignupForm.getFirstName());
 	}

	@Test(expected=InvalidEmailException.class) 
	public void emailAlreadyUsed() {   
		
		User mockUser = Mockito.mock(User.class);
		when(userDao.findByEmail(anyString())).thenReturn(mockUser);
		SignupForm testForm = new SignupForm();
		testForm.setId(1);
		testForm.setFirstName("testFirstName");
		testForm.setLastName("testLastName");
		testForm.setEmail("alreadyUsedEmail@test.test");
		testForm.setPassword("testtest");
		testForm.setConfirmPassword("testtest");
		testForm.setTutor(true);
		
		registerService.saveFrom(testForm);
	
	} 

}