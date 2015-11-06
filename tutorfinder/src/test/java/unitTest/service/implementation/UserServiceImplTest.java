package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class UserServiceImplTest {

	@Autowired
	UserDao userDao;
	@Autowired
	UserService userService;
	
	private User mockUser;
	private Principal mockAuthUser;
	
	@Before
	public void setUp() {
		this.mockUser = Mockito.mock(User.class);
		this.mockAuthUser = Mockito.mock(Principal.class);
	}
	
	
	@Test
	public void testSave() {
		//GIVEN
		when(userDao.save(any(User.class))).then(returnsFirstArg());
		
		//WHEN
		ReflectionTestUtils.setField(mockUser, "email", "user@test.ch");
		
		User tmpUser = userService.save(this.mockUser);
		
		//THEN
		assertEquals("user@test.ch", ReflectionTestUtils.getField(tmpUser, "email"));
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
		when(userDao.findById(anyLong())).thenReturn(this.mockUser);
		
		//WHEN		
		User tmpUser = userService.getUserById(new Long(1));
		
		//THEN
		assertEquals(tmpUser, this.mockUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetUserByIdNull() {
		userService.getUserById(null);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetNullUserById() {
		//GIVEN
		when(userDao.findOne(anyLong())).thenReturn(null);
				
		//WHEN
		userService.getUserById(new Long(1));
	}
	
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
}
