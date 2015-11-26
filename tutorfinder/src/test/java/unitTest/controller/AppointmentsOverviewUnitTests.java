package unitTest.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import ch.unibe.ese.Tutorfinder.controller.AppointmentsOverviewController;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.RateTutorForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml"})
public class AppointmentsOverviewUnitTests {
	
	private AppointmentsOverviewController controller;
	
	@Mock
	private Principal mockAuthUser;
	@Mock
	private User mockTutor;
	@Mock
	private User mockStudent;
	@Mock
	private Appointment mockApp;
	@Mock
	private Appointment mockAppAvailable;
	@Mock
	private AppointmentService mockAppointmentService;
	@Mock
	private UserService mockUserService;
	@Mock
	private HttpServletRequest mockReq;
	@Mock
	private BindingResult mockBindingResult;
	
	private List<Appointment> appList;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockAppAvailable.setAvailability(Availability.AVAILABLE);
		
		appList = new ArrayList<Appointment>();
		appList.add(mockApp);
		appList.add(mockAppAvailable);
		
		controller = new AppointmentsOverviewController(mockAppointmentService, mockUserService);
	}
	
	@Test
	public void testAppointments() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockTutor);
		when(mockAppointmentService.getFutureAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPendingAppointments(eq(mockTutor))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointmentsAsStudent(eq(mockTutor))).thenReturn(appList);
		
		ModelAndView gotMav = controller.appointments(mockAuthUser);
		
		assertTrue(gotMav.getModel().containsKey("authUser"));
		assertTrue(gotMav.getModel().containsKey("arranged"));
		assertTrue(gotMav.getModel().containsKey("reserved"));
		assertTrue(gotMav.getModel().containsKey("past"));
		assertTrue(gotMav.getModel().containsKey("pending"));
		assertTrue(gotMav.getModel().containsKey("visited"));
		assertTrue(gotMav.getModel().containsKey("rateTutorForm"));
	}
	
	@Test
	public void testEditAppointmentsDecline() {
		when(mockReq.getParameter("decline")).thenReturn("1");
		when(mockAppointmentService.updateAppointment(any(Availability.class), anyLong())).thenReturn(mockAppAvailable);
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockTutor);
		when(mockAppointmentService.getFutureAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPendingAppointments(eq(mockTutor))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointmentsAsStudent(eq(mockTutor))).thenReturn(appList);
		
		ModelAndView gotMav = controller.decline(mockReq, mockAuthUser);
		
		assertTrue(gotMav.getModel().containsKey("authUser"));
		assertTrue(gotMav.getModel().containsKey("arranged"));
		assertTrue(gotMav.getModel().containsKey("reserved"));
		assertTrue(gotMav.getModel().containsKey("past"));
		assertTrue(gotMav.getModel().containsKey("pending"));
		assertTrue(gotMav.getModel().containsKey("visited"));
		assertTrue(gotMav.getModel().containsKey("rateTutorForm"));
	}
	
	@Test
	public void testEditAppointmentsConfirm() {
		when(mockReq.getParameter("confirm")).thenReturn("1");
		when(mockAppointmentService.updateAppointment(any(Availability.class), anyLong())).thenReturn(mockAppAvailable);
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockTutor);
		when(mockAppointmentService.getFutureAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPendingAppointments(eq(mockTutor))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointmentsAsStudent(eq(mockTutor))).thenReturn(appList);
		
		ModelAndView gotMav = controller.confirm(mockReq, mockAuthUser);
		
		assertTrue(gotMav.getModel().containsKey("authUser"));
		assertTrue(gotMav.getModel().containsKey("arranged"));
		assertTrue(gotMav.getModel().containsKey("reserved"));
		assertTrue(gotMav.getModel().containsKey("past"));
		assertTrue(gotMav.getModel().containsKey("pending"));
		assertTrue(gotMav.getModel().containsKey("visited"));
		assertTrue(gotMav.getModel().containsKey("rateTutorForm"));
	}
	
	@Test
	public void testRateAppointment() {
		when(mockReq.getParameter("rate")).thenReturn("1");
		when(mockAppointmentService.updateAppointment(any(Availability.class), anyLong())).thenReturn(mockAppAvailable);
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockTutor);
		when(mockAppointmentService.getFutureAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointments(eq(mockTutor), any(Availability.class))).thenReturn(appList);
		when(mockAppointmentService.getPendingAppointments(eq(mockTutor))).thenReturn(appList);
		when(mockAppointmentService.getPastAppointmentsAsStudent(eq(mockTutor))).thenReturn(appList);
		
		RateTutorForm rateTutorForm = new RateTutorForm();
		rateTutorForm.setRating(BigDecimal.ONE);
		
		ModelAndView gotMav = controller.rate(mockReq, mockAuthUser, rateTutorForm, mockBindingResult);
		
		assertTrue(gotMav.getModel().containsKey("authUser"));
		assertTrue(gotMav.getModel().containsKey("arranged"));
		assertTrue(gotMav.getModel().containsKey("reserved"));
		assertTrue(gotMav.getModel().containsKey("past"));
		assertTrue(gotMav.getModel().containsKey("pending"));
		assertTrue(gotMav.getModel().containsKey("visited"));
		assertTrue(gotMav.getModel().containsKey("rateTutorForm"));
	}
}
