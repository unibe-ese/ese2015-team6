package unitTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.FindTutorController;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml" })
public class FindTutorControllerUnitTests {
	private FindTutorController findTutor;
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
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		findTutor = new FindTutorController(mockFindTutorService, mockUserService, mockFilterForm);
	}
	
	@Test
	public void findTutorWithoutQuery() {
		ModelAndView got = findTutor.findTutor(mockPrincipal, null);
		
		verify(mockFindTutorService, never()).generateComparatorFrom(any(FindTutorFilterForm.class));
		
		assertFalse(got.getModel().containsKey("Result"));
	}
	
	@Test
	public void findTutorWithEmptyQuery() {
		ModelAndView got = findTutor.findTutor(mockPrincipal, "");
		
		verify(mockFindTutorService, never()).generateComparatorFrom(any(FindTutorFilterForm.class));
		
		assertFalse(got.getModel().containsKey("Result"));
	}
	
	@Test
	public void findTutorWithQuery() {
		String testQuery = new String("testQuery");
		when(mockFindTutorService.getSubjectsSorted(testQuery)).thenReturn(mockMap);
		
		ModelAndView got = findTutor.findTutor(mockPrincipal, testQuery);
		
		verify(mockFindTutorService).generateComparatorFrom(any(FindTutorFilterForm.class));
		
		assertEquals(mockMap, got.getModel().get("Result"));
	}
}
