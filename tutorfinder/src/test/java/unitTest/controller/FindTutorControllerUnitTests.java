package unitTest.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.swing.SortOrder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.FindTutorController;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.SortCriteria;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml" })
public class FindTutorControllerUnitTests {
	
	private FindTutorController findTutorController;
	String testQuery = "testQuery";
	
	@Mock
	private FindTutorService mockFindTutorService;
	@Mock
	private UserService mockUserService;
	@Mock
	private FindTutorFilterForm mockFilterForm;
	@Mock
	private Principal mockPrincipal;
	@Mock
	private Map<User, List<Subject>> mockMap;
	@Mock
	private User mockUser;
	@Mock
	private List<Subject> mockSubjectList;
	@Mock
	private BindingResult mockBindingResult;
	@Mock
	private RedirectAttributes mockRedirectAttributes;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		findTutorController = new FindTutorController(mockFindTutorService, mockUserService, mockFilterForm);
	}
	
	@Test
	public void findTutorWithoutQuery() {
		ModelAndView got = findTutorController.findTutor(mockPrincipal, null);
		
		verify(mockFindTutorService, never()).generateComparatorFrom(any(FindTutorFilterForm.class));
		
		assertFalse(got.getModel().containsKey("Result"));
	}
	
	@Test
	public void findTutorWithEmptyQuery() {
		ModelAndView got = findTutorController.findTutor(mockPrincipal, "");
		
		verify(mockFindTutorService, never()).generateComparatorFrom(any(FindTutorFilterForm.class));
		
		assertFalse(got.getModel().containsKey("Result"));
	}
	
	@Test
	public void findTutorWithQuery() {
		when(mockFindTutorService.getSubjectsSorted(testQuery)).thenReturn(mockMap);
		
		ModelAndView got = findTutorController.findTutor(mockPrincipal, testQuery);
		
		verify(mockFindTutorService).generateComparatorFrom(any(FindTutorFilterForm.class));
		
		assertEquals(mockMap, got.getModel().get("Result"));
	}
	
	@Test
	public void submitWithQuery() {
		String got = findTutorController.submit(testQuery, mockFilterForm, mockBindingResult, mockRedirectAttributes);
		
		verify(mockRedirectAttributes).addFlashAttribute(any(String.class), eq(mockBindingResult));
		verify(mockFilterForm).setCriteria(any(SortCriteria.class));
		verify(mockFilterForm).setOrder(any(SortOrder.class));
		
		String[] split = got.split("=");
		assertTrue(split[0].contains("redirect:"));
		assertEquals(testQuery, split[1]);
	}
	
	@Test
	public void submitWithoutQuery() {
		String got = findTutorController.submit(null, mockFilterForm, mockBindingResult, mockRedirectAttributes);
		
		verify(mockRedirectAttributes).addFlashAttribute(any(String.class), eq(mockBindingResult));
		verify(mockFilterForm).setCriteria(any(SortCriteria.class));
		verify(mockFilterForm).setOrder(any(SortOrder.class));
		
		assertTrue(got.contains("redirect:"));
	}
	
	@Test(expected=AssertionError.class)
	public void submitWithFormErrors() {
		when(mockBindingResult.hasErrors()).thenReturn(true);
		findTutorController.submit(null, mockFilterForm, mockBindingResult, mockRedirectAttributes);
	}
}
