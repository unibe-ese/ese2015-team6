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
		assertEquals("redirect:login", got);
	}
	
	@Test
	public void testRegister() {
		String got = controller.register();
		
		assertFalse(got.isEmpty());
		assertEquals("redirect:/login?register", got);
	}

	@Test
	public void testCreate() {
		when(mockRegisterService.saveFrom(any(SignupForm.class))).thenReturn(mockSignupForm);

		controller.create(mockSignupForm, mockResult, mockRedirectAttributes);
		
		verify(mockRedirectAttributes, never()).addFlashAttribute(any(Object.class));

	}

	@Test
	public void testCreateWithInvalidEmail() {
		when(mockRegisterService.saveFrom(any(SignupForm.class))).thenThrow(new InvalidEmailException("TestException"));

		controller.create(mockSignupForm, mockResult, mockRedirectAttributes);

		verify(mockResult).rejectValue(eq("email"), any(String.class), any(String.class));
		verify(mockRedirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.signupForm", mockResult);
	}

	@Test
	public void testCreateWithErrors() {
		when(mockResult.hasErrors()).thenReturn(true);

		controller.create(mockSignupForm, mockResult, mockRedirectAttributes);

		verify(mockResult, never()).rejectValue(any(String.class), any(String.class), any(String.class));
		verify(mockRedirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.signupForm", mockResult);
	}
	
	@Test
	public void testLoginWithAuthUser() {
		ModelAndView got = controller.login(this.mockPrincipal, null, null, null, null);
		
		assertEquals("redirect:/findTutor", got.getViewName());
	}
	@Test
	public void testLoginParamSucces() {
		ModelAndView got = controller.login(null, null, null, null, "success");
		
		assertEquals("login", got.getViewName());
		assertTrue(got.getModel().containsKey("msg"));
	}
	
}
