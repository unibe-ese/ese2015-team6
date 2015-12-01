package unitTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.UpdateProfileController;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidProfileException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.PasswordConfirmationForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml"})
public class UpdateProfileControllerUnitTests {

	UpdateProfileController controller;
	
	@Mock
	private Principal mockAuthUser;
	@Mock
	private User mockUser;
	@Mock
	private UserService mockUserService;
	@Mock
	private ProfileService mockProfileService;
	@Mock
	private PrepareFormService mockPrepareFormService;
	@Mock
	private HttpServletRequest mockReq;
	@Mock
	private BindingResult mockBindingResult;
	@Mock
	private RedirectAttributes mockRedirectAttributs;
	@Mock
	private ModelAndView mockModel;
	@Mock
	private AuthenticationManager mockAuthenticationManager;
	
	private UpdateProfileForm updateProfileForm = new UpdateProfileForm();
	
	private PasswordConfirmationForm passwordConfirmationForm = new PasswordConfirmationForm();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		controller = new UpdateProfileController(this.mockUserService, this.mockProfileService, 
				this.mockPrepareFormService, this.mockAuthenticationManager);
		
		this.updateProfileForm.setFirstName("testFirstName");
		this.updateProfileForm.setLastName("testLastName");
		this.updateProfileForm.setBiography("testBiography");
		this.updateProfileForm.setId(new Long(1));
		this.updateProfileForm.setRegion("testRegion");
		this.updateProfileForm.setWage(new BigDecimal(50));
		
		this.passwordConfirmationForm.setPassword("testPassword");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encPwd = encoder.encode("testPassword");
		ReflectionTestUtils.setField(this.mockUser, "password", encPwd);
		ReflectionTestUtils.setField(this.mockUser, "email", "test@example.ch");
		
	}
	
	@Test
	public void testEditProfile() {
		when(mockPrepareFormService.prepareForm(eq(mockAuthUser), any(ModelAndView.class))).thenReturn(this.mockModel);
		
		ModelAndView gotMav = controller.editProfile(this.mockAuthUser);
		
		assertEquals(mockModel, gotMav);
	}
	
	@Test
	public void testUpdate() {
		when(mockUserService.getUserByPrincipal(eq(mockAuthUser))).thenReturn(this.mockUser);
		when(mockPrepareFormService.prepareForm(eq(mockAuthUser), any(ModelAndView.class))).thenReturn(this.mockModel);
		when(mockBindingResult.hasErrors()).thenReturn(false);
		
		controller.update(this.mockAuthUser, this.updateProfileForm, 
				this.mockBindingResult, this.mockRedirectAttributs);
		
		verify(mockProfileService).saveFrom(eq(this.updateProfileForm), eq(mockUser));
		verify(mockModel).addObject(eq("updateProfileForm"), eq(this.updateProfileForm));
	}
	
	@Test
	public void testUpdateWhenHasErrors() {
		when(mockPrepareFormService.prepareForm(eq(mockAuthUser), any(ModelAndView.class))).thenReturn(this.mockModel);
		when(mockBindingResult.hasErrors()).thenReturn(true);
		
		controller.update(this.mockAuthUser, this.updateProfileForm, 
				this.mockBindingResult, this.mockRedirectAttributs);
		
		verify(mockModel).addObject(eq("updateProfileForm"), eq(this.updateProfileForm));
	}
	
	@Test
	public void testUpdateWhenThrowInvalidProfileException() {
		when(mockUserService.getUserByPrincipal(eq(mockAuthUser))).thenReturn(this.mockUser);
		when(mockProfileService.saveFrom(any(UpdateProfileForm.class), eq(mockUser))).thenThrow(new InvalidProfileException("TestException"));
		when(mockPrepareFormService.prepareForm(eq(mockAuthUser), any(ModelAndView.class))).then(returnsSecondArg());
		when(mockBindingResult.hasErrors()).thenReturn(false);
		
		ModelAndView gotMav = controller.update(this.mockAuthUser, this.updateProfileForm, 
				this.mockBindingResult, this.mockRedirectAttributs);
		
		verify(mockProfileService).saveFrom(eq(this.updateProfileForm), eq(mockUser));
		assertTrue(gotMav.getModel().containsKey("page_error"));
		assertTrue(gotMav.getModel().containsKey("updateProfileForm"));
	}
	
	@Test
	public void testBecomeTutor() {
		when(mockUserService.getUserByPrincipal(eq(mockAuthUser))).thenReturn(this.mockUser);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encPwd = encoder.encode("testPassword");
		when(mockUserService.changeToTutor(eq(mockUser))).thenReturn(this.mockUser);
		when(mockUser.getPassword()).thenReturn(encPwd);
		when(mockUser.getEmail()).thenReturn("test@example.ch");
		when(mockPrepareFormService.prepareForm(any(Authentication.class), any(ModelAndView.class))).then(returnsSecondArg());
		when(mockAuthenticationManager.authenticate(any(Authentication.class))).thenReturn(
				new UsernamePasswordAuthenticationToken("test@example.ch", "testPassword"));
		
		ModelAndView gotMav = controller.becomeTutor(this.mockAuthUser, this.passwordConfirmationForm, 
				mockBindingResult, this.mockRedirectAttributs);
		
		verify(mockUserService).getUserByPrincipal(eq(mockAuthUser));
		verify(mockUserService).changeToTutor(eq(mockUser));
		verify(mockPrepareFormService).prepareForm(any(Authentication.class), any(ModelAndView.class));
		assertEquals("updateProfile", gotMav.getViewName());
	}
	
	@Test
	public void testBecomeTutorIfPasswordDoesNotMatch() {
		when(mockUserService.getUserByPrincipal(eq(mockAuthUser))).thenReturn(this.mockUser);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encPwd = encoder.encode("Password");
		when(mockUser.getPassword()).thenReturn(encPwd);
		when(mockPrepareFormService.prepareForm(eq(mockAuthUser), any(ModelAndView.class))).then(returnsSecondArg());
		
		ModelAndView gotMav = controller.becomeTutor(this.mockAuthUser, this.passwordConfirmationForm, 
				mockBindingResult, this.mockRedirectAttributs);
		
		verify(mockUserService).getUserByPrincipal(eq(mockAuthUser));
		verify(mockPrepareFormService).prepareForm(eq(mockAuthUser), any(ModelAndView.class));
		assertEquals("updateProfile", gotMav.getViewName());
	}
	
	@Test
	public void testBecomeTutorWhenPasswordIsNull() {
		this.passwordConfirmationForm.setPassword(null);
		when(mockUserService.getUserByPrincipal(eq(mockAuthUser))).thenReturn(this.mockUser);
		
		ModelAndView gotMav = controller.becomeTutor(this.mockAuthUser, this.passwordConfirmationForm, 
				mockBindingResult, this.mockRedirectAttributs);
		
		verify(mockUserService).getUserByPrincipal(eq(mockAuthUser));
		assertEquals("updateProfile", gotMav.getViewName());
	}
}
