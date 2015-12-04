package integrationTest.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration 
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 
@Transactional
public class ShowProfileTest {
	
	@Autowired 
	private WebApplicationContext wac;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfileDao profileDao;
	
	private MockMvc mockMvc;
	
	@BeforeClass
    public static void baseSetUp() {
    	TestUtility.initialize();
    }
    
	@Before 
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(this.wac)
				.apply(springSecurity())
				.build();
		TestUtility.setUp(userDao, profileDao);
	}

	@Test
	@WithMockUser(username=TestUtility.FIRST_USER_USERNAME, roles=ConstantVariables.TUTOR)
	public void showProfileWithoutParams() throws Exception {
		mockMvc.perform(get("/showProfile"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("makeAppointmentsForm"))
			.andExpect(model().attribute("DisplayedUser", TestUtility.testUser));
	}
	
	@Test
	@WithMockUser(username=TestUtility.FIRST_USER_USERNAME, roles=ConstantVariables.TUTOR)
	public void showProfileWithIdAndDate() throws Exception {
		mockMvc.perform(get("/showProfile")
			.param("userId", String.valueOf(userDao.findByEmail(TestUtility.SECOND_USER_USERNAME).getId()))
			.param("date", LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("makeAppointmentsForm"))
				.andExpect(model().attribute("DisplayedUser", TestUtility.testUserTwo));;
	}

	@Test
	@WithMockUser(username=TestUtility.FIRST_USER_USERNAME, roles=ConstantVariables.TUTOR)
	public void redirectShowProfileWithId() throws Exception {
		mockMvc.perform(get("/showProfile")
			.param("userId", String.valueOf(userDao.findByEmail(TestUtility.SECOND_USER_USERNAME).getId())))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("showProfile?userId="+TestUtility.testUserTwo.getId()+"&date="+LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
	}
	
	//TODO Make it work @Test
	@WithMockUser(username=TestUtility.FIRST_USER_USERNAME, roles=ConstantVariables.TUTOR)
	public void redirectShowProfileWithIdAndFlashAttribute() throws Exception {
		mockMvc.perform(get("/showProfile")
				.param("userId", String.valueOf(userDao.findByEmail(TestUtility.SECOND_USER_USERNAME).getId()))
				.flashAttr("testAttribute", "test"))
					.andExpect(status().isOk())
					.andExpect(forwardedUrl("showProfile?userId="+TestUtility.testUserTwo.getId()+"&date="+LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
					.andExpect(flash().attributeExists("testAttribute"));
	}

}
