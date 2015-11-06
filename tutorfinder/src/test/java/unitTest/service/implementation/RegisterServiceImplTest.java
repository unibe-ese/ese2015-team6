package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
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
	
	private SignupForm signupForm;
	
	@Before
	public void setUp() {
		this.signupForm = new SignupForm();
		this.signupForm.setId(new Long(1));
		this.signupForm.setEmail("form@test.ch");
		this.signupForm.setFirstName("testFirstName");
		this.signupForm.setLastName("testLastName");
		this.signupForm.setPassword("testPassword");
		this.signupForm.setConfirmPassword("testPassword");
		this.signupForm.setTutor(true);
	}
	 
	@Test
	public void testSaveFormForTutor() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(null);
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN		
		SignupForm tmpSignupForm = registerService.saveFrom(this.signupForm);
		
		//THEN
		assertEquals("testFirstName", tmpSignupForm.getFirstName());
 
	}
	 
	@Test
	public void testSaveFormForStudent() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(null);
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		this.signupForm.setTutor(false);
		
		SignupForm tmpSignupForm = registerService.saveFrom(this.signupForm);
		
		//THEN
		assertEquals("testFirstName", tmpSignupForm.getFirstName());
 	}

	@Test(expected=InvalidEmailException.class) 
	public void emailAlreadyUsed() {   
		//GIVEN
		User mockUser = Mockito.mock(User.class);
		when(userDao.findByEmail(anyString())).thenReturn(mockUser);
		
		//WHEN
		this.signupForm.setEmail("alreadyUsedEmail@test.test");
		
		registerService.saveFrom(this.signupForm);
	
	} 

}