package integrationTest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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

import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration 
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 
@Transactional
public class FindTutorControllerTest {

	@Autowired 
	private WebApplicationContext wac;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfileDao profileDao;
	
	private MockMvc mockMvc;
	Principal authUser;
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
	public void findTutorWithNoQuery() throws Exception {
		mockMvc.perform(get("/findTutor").principal(this.authUserStudent))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attributeExists("findTutorFilterForm"))
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attributeExists("formaction"));
	}
	
}
