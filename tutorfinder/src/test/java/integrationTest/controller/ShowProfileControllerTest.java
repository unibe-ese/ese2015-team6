package integrationTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.Availability;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/testData.xml", "file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@Transactional
public class ShowProfileControllerTest {

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfileDao profileDao;
	@Autowired
	private AppointmentDao appointmentDao;

	private MockMvc mockMvc;
	private Principal authUser;

	@BeforeClass
	public static void baseSetUp() {
		TestUtility.initialize();
	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		TestUtility.setUp(userDao, profileDao);
		this.authUser = TestUtility.createPrincipal(TestUtility.testUser.getEmail(), "password", "ROLE_TUTOR");
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void showProfileWithoutParams() throws Exception {
		this.mockMvc.perform(
			get("/showProfile").principal(this.authUser))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("makeAppointmentsForm"))
		.andExpect(view().name("showProfile"))
		.andExpect(model().attribute("DisplayedUser", TestUtility.testUser));
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void showProfileWithIdAndDate() throws Exception {
		this.mockMvc.perform(
			get("/showProfile").principal(this.authUser)
			.param("userId", String.valueOf((TestUtility.testUserTwo).getId()))
			.param("date", LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
		.andExpect(status().isOk())
		.andExpect(view().name("showProfile")).andExpect(model().attributeExists("makeAppointmentsForm"))
		.andExpect(model().attribute("DisplayedUser", TestUtility.testUserTwo));
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void redirectShowProfileWithId() throws Exception {
		this.mockMvc.perform(
			get("/showProfile").principal(this.authUser)
			.param("userId", String.valueOf((TestUtility.testUserTwo).getId())))
		.andExpect(status().isOk())
		.andExpect(forwardedUrl("showProfile?userId=" + TestUtility.testUserTwo.getId() + "&date="
			+ LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void showProfileWithDate() throws Exception {
		this.mockMvc.perform(
			get("/showProfile").principal(this.authUser)
			.param("date", LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
		.andExpect(status().isOk())
		.andExpect(view().name("showProfile"))
		.andExpect(model().attributeExists("makeAppointmentsForm"))
		.andExpect(model().attribute("DisplayedUser", TestUtility.testUser));
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void testShowProfileWithFlashAttribute() throws Exception {
		this.mockMvc.perform(
			get("/showProfile").principal(this.authUser)
			.flashAttr("testAttribute", "test"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("makeAppointmentsForm"))
		.andExpect(view().name("showProfile"))
		.andExpect(model().attribute("DisplayedUser", TestUtility.testUser))
		.andExpect(model().attributeExists("testAttribute"));
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void redirectShowProfileWithIdAndFlashAttribute() throws Exception {
		this.mockMvc.perform(
			get("/showProfile").principal(this.authUser)
			.param("userId", String.valueOf((TestUtility.testUserTwo).getId()))
			.flashAttr("testAttribute", "test"))
		.andExpect(status().isOk())
		.andExpect(forwardedUrl("showProfile?userId=" + TestUtility.testUserTwo.getId() + "&date="
				+ LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
		.andExpect(model().attributeExists("testAttribute"));
	}

	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void testRequestAppointment() throws Exception {
		this.mockMvc.perform(
			post("/updateForm").principal(this.authUser)
			.param("userId", String.valueOf(TestUtility.testUserTwo.getId()))
			.param("request", String.valueOf(LocalTime.now().getHour()))
			.param("date", String.valueOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE))))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("makeAppointmentsForm"))
		.andExpect(view().name("showProfile"))
		.andExpect(model().attribute("DisplayedUser", TestUtility.testUserTwo));
		
		List<Appointment> tmpAppointmentList = appointmentDao.findAllByTutor(TestUtility.testUserTwo);
		assertFalse(tmpAppointmentList.isEmpty());
		assertEquals(1, tmpAppointmentList.size());
		
		Appointment tmpAppointment = tmpAppointmentList.get(0);
		assertEquals(TestUtility.testUserTwo, tmpAppointment.getTutor());
		assertEquals(TestUtility.testUser, tmpAppointment.getStudent());
		assertEquals(Availability.RESERVED, tmpAppointment.getAvailability());
		assertEquals(LocalDate.now().getDayOfWeek(), tmpAppointment.getDay());
		assertEquals(ConstantVariables.MIN_WAGE, tmpAppointment.getWage());
		assertEquals(LocalDate.now(), tmpAppointment.getTimestamp().toLocalDateTime().toLocalDate());
		assertEquals(LocalTime.now().getHour(), tmpAppointment.getTimestamp().toLocalDateTime().getHour());
	}
	
	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void testGetDate() throws Exception {
		MakeAppointmentsForm appForm = new MakeAppointmentsForm();
		appForm.setDate(LocalDate.now());
		this.mockMvc.perform(
				post("/updateForm").principal(this.authUser)
				.param("userId", String.valueOf(TestUtility.testUserTwo.getId()))
				.param("getDate", "true")
				.param("date", String.valueOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE))))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("showProfile?userId=" + TestUtility.testUserTwo.getId() + "&date="
				+ LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
		.andExpect(view().name("redirect:showProfile?userId=" + TestUtility.testUserTwo.getId() + "&date="
				+ LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
	}
	
	@Test
	@WithMockUser(roles = ConstantVariables.TUTOR)
	public void testGetDateWithWrongInput() throws Exception {
		this.mockMvc.perform(
				post("/updateForm").principal(this.authUser)
				.param("userId", String.valueOf(TestUtility.testUserTwo.getId()))
				.param("getDate", "true")
				.param("date", "wrong input"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("showProfile?userId=" + TestUtility.testUserTwo.getId()))
		.andExpect(view().name("redirect:showProfile?userId=" + TestUtility.testUserTwo.getId()))
		.andExpect(flash().attribute("error_message", "Enter a valid date (yyyy-MM-dd)"));
	}
	

}
