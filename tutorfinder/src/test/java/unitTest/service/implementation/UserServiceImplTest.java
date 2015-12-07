package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.security.Principal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class UserServiceImplTest {

	@Autowired
	UserDao userDao;
	@Autowired
	UserService userService;
	
	@Mock
	private User mockUser;
	@Mock
	private Principal mockAuthUser;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
	}
	
	@After
	public void reset() {
		Mockito.reset(userDao);
	}
	
	
	@Test
	public void testSave() {
		//GIVEN
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		ReflectionTestUtils.setField(mockUser, "email", "user@test.ch");
		
		User tmpUser = userService.save(this.mockUser);
		
		//THEN
		assertEquals(this.mockUser.getEmail(), tmpUser.getEmail());
	}
	
	@Test(expected=AssertionError.class)
	public void testSaveNull() {
		userService.save(null);
	}
	
	@Test(expected=AssertionError.class)
	public void testSaveUserReturnsNull() {
		//GIVEN
		when(userDao.save(any(User.class))).thenReturn(null);
		
		//WHEN
		userService.save(this.mockUser);
	}
	
	@Test
	public void testGetUserById() {
		//GIVEN
		when(userDao.findOne(anyLong())).thenReturn(this.mockUser);
		
		//WHEN		
		User tmpUser = userService.getUserById(Long.valueOf(1));
		
		//THEN
		assertEquals(tmpUser, this.mockUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetUserByIdNull() {
		userService.getUserById(null);
	}
	
//	@Test(expected=AssertionError.class)
//	public void testGetNullUserById() {
//		//GIVEN
//		when(userDao.findOne(anyLong())).thenReturn(null);
//				
//		//WHEN
//		userService.getUserById(Long.valueOf(1));
//	}
	
	@Test
	public void testGetUserByPrincipal() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		
		//WHEN		
		User tmpUser = userService.getUserByPrincipal(this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUser, this.mockUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetUserByPrincipalNull() {
		userService.getUserByPrincipal(null);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetNullUserByPrincipal() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(null);
				
		//WHEN
		userService.getUserByPrincipal(this.mockAuthUser);
	}
	
	@Test
	public void testChangeToTutorWhenStudent() {
		when(userDao.save(mockUser)).then(returnsFirstArg());
		when(mockUser.getRole()).thenReturn(ConstantVariables.STUDENT);
		
		userService.changeToTutor(this.mockUser);
		
		verify(mockUser).setRole(ConstantVariables.TUTOR);
	}
	
	@Test
	public void testChangeToTutorWhenTutor() {
		when(mockUser.getRole()).thenReturn(ConstantVariables.TUTOR);
		
		User tmpUser = userService.changeToTutor(this.mockUser);
		
		assertEquals(this.mockUser, tmpUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testChangedToTutorWhenNull() {
		userService.changeToTutor(null);
	}
}
