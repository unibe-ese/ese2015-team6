package Service.implementation.UnitTests;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;
import ch.unibe.ese.Tutorfinder.controller.service.RegisterService;
import ch.unibe.ese.Tutorfinder.controller.service.implementations.RegisterServiceImpl;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class RegisterServiceImplTest {
	
	@Configuration
    static class ContextConfiguration {

	     @Bean
	     public UserDao userDaoMock() {
	    	 UserDao userDao = mock(UserDao.class);
	    	 return userDao;
	     }
        @Bean
        public RegisterService registerFormService() {
            RegisterService registerFormService = new RegisterServiceImpl();
            return registerFormService;
        }
    }
	
	@Autowired private RegisterService registerService; 
	@Qualifier("userDaoMock")
	@Autowired private UserDao userDao;   
	
	    

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