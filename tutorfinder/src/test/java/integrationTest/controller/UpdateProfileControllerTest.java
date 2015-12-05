package integrationTest.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Before;
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

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.model.dao.BillDao;
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
public class UpdateProfileControllerTest {
	
	@Autowired 
	private WebApplicationContext wac;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfileDao profileDao;

	private MockMvc mockMvc;
	private Principal authUser;
	private Principal authUserStudent;

	@Before 
	public void setUp() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.wac).build();
		TestUtility.initialize();
		TestUtility.setUp(userDao, profileDao);
		this.authUser = TestUtility.createPrincipal(TestUtility.testUser.getEmail(), "password", "ROLE_TUTOR");
		this.authUserStudent = TestUtility.createPrincipal(TestUtility.testUserThree.getEmail(), "password", "ROLE_STUDENT");
	} 
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void mappingEditProfile() throws Exception {
		mockMvc.perform(get("/editProfile").principal(this.authUser))
				.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles="Tutor")
	public void validUpdate() throws Exception {
		mockMvc.perform(post("/update").principal(this.authUser)
				.param("firstName", "newFirstName")
				.param("lastName", "newLastName")
				.param("wage", "10.0"))
		.andExpect(status().is3xxRedirection());
		
		User referenceUser = userDao.findByEmail(TestUtility.testUser.getEmail());
		
		assertEquals("newFirstName" , referenceUser.getFirstName());
		assertEquals("newLastName" , referenceUser.getLastName());
	}	
	
	@Test
	@WithMockUser(roles="Student")
	public void changeRole() throws Exception {
		
		mockMvc.perform(post("/changeRole").principal(authUserStudent)
				.param("password", "password"))
		.andExpect(status().isOk());
		
		User referenceUser = userDao.findByEmail(TestUtility.testUserThree.getEmail());
		
		assertEquals(ConstantVariables.TUTOR , referenceUser.getRole());
		
	}
	
}
