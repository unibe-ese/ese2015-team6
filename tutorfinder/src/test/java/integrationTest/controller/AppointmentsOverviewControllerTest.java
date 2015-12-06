package integrationTest.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.Availability;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 

@Transactional
public class AppointmentsOverviewControllerTest {

	@Autowired private WebApplicationContext wac; 
	@Autowired private UserDao userDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private AppointmentDao appointmentDao;

	private MockMvc mockMvc;
	private Principal authUser;
	private Appointment appointment;
	private List<Appointment> emptyAppList;
	private List<Appointment> appList;


	@BeforeClass
	public static void basicSetUp() {
		TestUtility.initialize();
	}

	@Before 
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); 
		TestUtility.initialize();
		TestUtility.setUp(userDao, profileDao);
		this.authUser = TestUtility.createPrincipal(TestUtility.testUser.getEmail(), "password", "ROLE_TUTOR");
		
		this.appointment = new Appointment();
		this.appList = new ArrayList<Appointment>();
		this.emptyAppList = new ArrayList<Appointment>();
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointments() throws Exception {
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
	}
	
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointmentsWithReserverd() throws Exception {
		this.appointment.setTutor(TestUtility.testUser);
		this.appointment.setStudent(TestUtility.testUserTwo);
		this.appointment.setAvailability(Availability.RESERVED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.appList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointmentsWithPending() throws Exception {
		this.appointment.setTutor(TestUtility.testUserTwo);
		this.appointment.setStudent(TestUtility.testUser);
		this.appointment.setAvailability(Availability.RESERVED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.appList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointmentsWithArranged() throws Exception {
		this.appointment.setTutor(TestUtility.testUser);
		this.appointment.setStudent(TestUtility.testUserTwo);
		this.appointment.setAvailability(Availability.ARRANGED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.appList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointmentsWithConfirmed() throws Exception {
		this.appointment.setTutor(TestUtility.testUserTwo);
		this.appointment.setStudent(TestUtility.testUser);
		this.appointment.setAvailability(Availability.ARRANGED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.appList))
		.andExpect(model().attribute("visited", this.emptyAppList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointmentsWithPast() throws Exception {
		this.appointment.setTutor(TestUtility.testUser);
		this.appointment.setStudent(TestUtility.testUserTwo);
		this.appointment.setAvailability(Availability.ARRANGED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(-1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.appList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAppointmentsWithVisited() throws Exception {
		this.appointment.setTutor(TestUtility.testUserTwo);
		this.appointment.setStudent(TestUtility.testUser);
		this.appointment.setAvailability(Availability.ARRANGED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(-1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				get("/appointments").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.appList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testeditAppointmentsDecline() throws Exception {
		this.appointment.setTutor(TestUtility.testUser);
		this.appointment.setStudent(TestUtility.testUserTwo);
		this.appointment.setAvailability(Availability.RESERVED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				post("/editAppointments").principal(this.authUser)
				.param("decline", String.valueOf(this.appointment.getId())))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
		
		List<Appointment> tmpAppList = appointmentDao.findAllByTutor(TestUtility.testUser);
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testeditAppointmentsConfirm() throws Exception {
		this.appointment.setTutor(TestUtility.testUser);
		this.appointment.setStudent(TestUtility.testUserTwo);
		this.appointment.setAvailability(Availability.RESERVED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				post("/editAppointments").principal(this.authUser)
				.param("confirm", String.valueOf(this.appointment.getId())))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.appList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.emptyAppList));
		
		Appointment tmpApp = appointmentDao.findOne(this.appointment.getId());
		assertEquals(Availability.ARRANGED, tmpApp.getAvailability());
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testRateAppointments() throws Exception {
		this.appointment.setTutor(TestUtility.testUserTwo);
		this.appointment.setStudent(TestUtility.testUser);
		this.appointment.setAvailability(Availability.ARRANGED);
		this.appointment.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(-1l)));
		this.appointment.setDay((appointment.getTimestamp()).toLocalDateTime().getDayOfWeek());
		this.appointment.setWage(ConstantVariables.MIN_WAGE);

		this.appointment = appointmentDao.save(this.appointment);
		this.appList.add(this.appointment);
		
		this.mockMvc
			.perform(
				post("/rateAppointment").principal(this.authUser)
				.param("rate", String.valueOf(this.appointment.getId()))
				.param("rating", String.valueOf(BigDecimal.valueOf(4.5))))
		.andExpect(status().isOk())
		.andExpect(view().name("appointmentsOverview"))
		.andExpect(model().attribute("arranged", this.emptyAppList))
		.andExpect(model().attribute("reserved", this.emptyAppList))
		.andExpect(model().attribute("past", this.emptyAppList))
		.andExpect(model().attribute("pending", this.emptyAppList))
		.andExpect(model().attribute("confirmed", this.emptyAppList))
		.andExpect(model().attribute("visited", this.appList));
		
		Appointment tmpApp = appointmentDao.findOne(this.appointment.getId());
		assertEquals(BigDecimal.valueOf(4.5), tmpApp.getRating());
	}
}
