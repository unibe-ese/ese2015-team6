package unitTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.LoginController;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;
import ch.unibe.ese.Tutorfinder.controller.service.RegisterService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml" })
public class LoginControllerUnitTests {

	private LoginController controller;

	@Mock
	private RegisterService mockRegisterService;
	@Mock
	private SignupForm mockSignupForm;
	@Mock
	private BindingResult mockResult;
	@Mock
	private RedirectAttributes mockRedirectAttributes;
	@Mock
	private HttpServletRequest mockRequest;
	@Mock
	private HttpServletResponse mockResponse;
	@Mock
	private SecurityContext mockContext;
	@Mock
	private Authentication mockAuthentication;
	@Mock
	private Principal mockPrincipal;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		controller = new LoginController(mockRegisterService);
	}

	@Test
	public void testHome() {
		String got = controller.home();

		assertFalse(got.isEmpty());
	}
	
	@Test
	public void testRegister() {
		String got = controller.register();
		
		assertFalse(got.isEmpty());
	}
	
	@Test
	public void testSuccess() {
		String got = controller.success();
		
		assertFalse(got.isEmpty());
	}
	
	@Test
	public void testLogoutWithoutAuthentication() {
		String got = controller.logoutPage(mockRequest, mockResponse);
		
		assertFalse(got.isEmpty());
	}

	@Test
	public void testCreate() {
		when(mockRegisterService.saveFrom(any(SignupForm.class))).thenReturn(mockSignupForm);

		ModelAndView got = controller.create(mockSignupForm, mockResult, mockRedirectAttributes);

		assertEquals("signupCompleted", got.getViewName());
	}

	@Test
	public void testCreateWithInvalidEmail() {
		when(mockRegisterService.saveFrom(any(SignupForm.class))).thenThrow(new InvalidEmailException("TestException"));

		ModelAndView got = controller.create(mockSignupForm, mockResult, mockRedirectAttributes);

		assertEquals("login", got.getViewName());
		verify(mockResult).rejectValue(eq("email"), any(String.class), any(String.class));
	}

	@Test
	public void testCreateWithErrors() {
		when(mockResult.hasErrors()).thenReturn(true);

		ModelAndView got = controller.create(mockSignupForm, mockResult, mockRedirectAttributes);

		assertEquals("login", got.getViewName());
		verify(mockResult, never()).rejectValue(any(String.class), any(String.class), any(String.class));
	}
	
	@Test
	public void testLogin() {
		ModelAndView got = controller.login(null, null, null);
		
		assertEquals("login", got.getViewName());
		assertTrue(got.getModel().containsKey("loginUrl"));
		assertTrue(got.getModel().containsKey("signupForm"));
		assertEquals("visible", got.getModel().get("loginBoxVisibility"));
		assertEquals("hidden", got.getModel().get("registerBoxVisibility"));
	}
	
	@Test
	public void testLoginParamError() {
		ModelAndView got = controller.login("error", null, null);
		
		assertEquals("login", got.getViewName());
		assertTrue(got.getModel().containsKey("loginUrl"));
		assertTrue(got.getModel().containsKey("signupForm"));
		assertTrue(got.getModel().containsKey("error"));
		assertEquals("visible", got.getModel().get("loginBoxVisibility"));
		assertEquals("hidden", got.getModel().get("registerBoxVisibility"));
	}
	
	@Test
	public void testLoginParamLogout() {
		ModelAndView got = controller.login(null, "logout", null);
		
		assertEquals("login", got.getViewName());
		assertTrue(got.getModel().containsKey("loginUrl"));
		assertTrue(got.getModel().containsKey("signupForm"));
		assertTrue(got.getModel().containsKey("msg"));
		assertEquals("visible", got.getModel().get("loginBoxVisibility"));
		assertEquals("hidden", got.getModel().get("registerBoxVisibility"));
	}
	
	@Test
	public void testLoginParamRegister() {
		ModelAndView got = controller.login(null, null, "register");
		
		assertEquals("login", got.getViewName());
		assertTrue(got.getModel().containsKey("loginUrl"));
		assertTrue(got.getModel().containsKey("signupForm"));
		assertEquals("hidden", got.getModel().get("loginBoxVisibility"));
		assertEquals("visible", got.getModel().get("registerBoxVisibility"));
	}

	@Test
	public void testAccessDenied() {
		ModelAndView got = controller.accessDenied(mockPrincipal);

		assertEquals("403", got.getViewName());
	}
	
}
