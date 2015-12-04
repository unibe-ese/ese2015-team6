package unitTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.ShowProfileController;
import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml"})
public class ShowProfileControllerUnitTests {

	private ShowProfileController controller;

	@Mock
	private Principal mockAuthUser;
	@Mock
	private User mockTutor;
	@Mock
	private User mockStudent;
	@Mock
	private AppointmentService mockAppointmentService;
	@Mock
	private UserService mockUserService;
	@Mock
	private ProfileService mockProfileService;
	@Mock
	private TimetableService mockTimetableService;
	@Mock
	private PrepareFormService mockPrepareService;
	@Mock
	private MakeAppointmentsForm mockAppointmentsForm;
	@Mock
	private HttpServletRequest mockReq;
	@Mock
	private BindingResult mockResult;
	@Mock
	private RedirectAttributes mockRedirectAttributes;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		controller = new ShowProfileController(mockAppointmentService, mockUserService,
				mockTimetableService, mockPrepareService);
	}

	@Test
	public void testProfile() {
		when(mockTutor.getId()).thenReturn(1l);
		when(mockPrepareService.prepareModelByUserId(eq(mockAuthUser), anyLong(), any(ModelAndView.class))).thenReturn(new ModelAndView());

		ModelAndView gotMav = controller.profile(mockAuthUser, mockTutor.getId(), null, mockRedirectAttributes);
		
		assertTrue(gotMav.getViewName().contains("forward:"));
	}
	
	@Test
	public void testRequestAppointment() {
		LocalDate date = LocalDate.now();
		List<Timetable> timetableList = new ArrayList<Timetable>();
		List<AppointmentPlaceholder> appointmentList = new ArrayList<AppointmentPlaceholder>();
		when(mockTutor.getId()).thenReturn(1l);
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(mockStudent);
		when(mockUserService.getUserById(anyLong())).thenReturn(mockTutor);
		when(mockReq.getParameter("request")).thenReturn("1");
		when(mockAppointmentService.saveFrom(mockAppointmentsForm, 1, mockTutor, mockStudent)).thenReturn(mockAppointmentsForm);
		when(mockAppointmentsForm.getDate()).thenReturn(date);
		when(mockTimetableService.findAllByUserAndDay(mockTutor, date.getDayOfWeek())).thenReturn(timetableList);
		when(mockAppointmentService.loadAppointments(null, mockTutor, date)).thenReturn(appointmentList);
		when(mockPrepareService.prepareModelByUserId(eq(mockAuthUser), anyLong(), any(ModelAndView.class))).thenReturn(new ModelAndView());
		
		ModelAndView gotMav = controller.requestAppointment(mockTutor.getId(), mockAppointmentsForm, mockReq, mockAuthUser, mockResult);
		
		assertEquals(mockAppointmentsForm, gotMav.getModel().get("makeAppointmentsForm"));
	}
	
	@Test
	public void testRequestAppointmentWithErrors() {
		when(mockTutor.getId()).thenReturn(1l);
		when(mockReq.getParameter("request")).thenReturn("1");
		when(mockResult.hasErrors()).thenReturn(true);
		when(mockPrepareService.prepareModelByUserId(eq(mockAuthUser), anyLong(), any(ModelAndView.class))).thenReturn(new ModelAndView());
		
		ModelAndView gotMav = controller.requestAppointment(mockTutor.getId(), mockAppointmentsForm, mockReq, mockAuthUser, mockResult);
		
		assertEquals(mockAppointmentsForm, gotMav.getModel().get("makeAppointmentsForm"));
	}
	
	@Test
	public void testGetDate() {
		LocalDate date = LocalDate.now();
		List<Timetable> timetableList = new ArrayList<Timetable>();
		List<AppointmentPlaceholder> appointmentList = new ArrayList<AppointmentPlaceholder>();
		when(mockTutor.getId()).thenReturn(1l);
		when(mockUserService.getUserById(anyLong())).thenReturn(mockTutor);
		when(mockAppointmentsForm.getDate()).thenReturn(date);
		when(mockTimetableService.findAllByUserAndDay(mockTutor, date.getDayOfWeek())).thenReturn(timetableList);
		when(mockAppointmentService.loadAppointments(null, mockTutor, date)).thenReturn(appointmentList);
		when(mockPrepareService.prepareModelByUserId(eq(mockAuthUser), anyLong(), any(ModelAndView.class))).then(AdditionalAnswers.returnsLastArg());
		
		String got = controller.getDate(mockTutor.getId(), mockAppointmentsForm, mockResult, mockRedirectAttributes);
		String dateString = got.substring(got.length()-10);
		assertEquals(date, LocalDate.parse(dateString));
	}
	
	@Test
	public void testGetDateWithErrors() {
		when(mockTutor.getId()).thenReturn(1l);
		when(mockResult.hasErrors()).thenReturn(true);
		when(mockRedirectAttributes.addFlashAttribute(any(String.class), any(String.class))).thenReturn(mockRedirectAttributes);
		
		String got = controller.getDate(mockTutor.getId(), mockAppointmentsForm, mockResult, mockRedirectAttributes);
		
		verify(mockRedirectAttributes).addFlashAttribute(any(String.class), any(String.class));
		assertTrue(got.contains("redirect:"));
	}

}
