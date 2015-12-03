package integrationTest.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration 
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 
@Transactional
public class LoginControllerTest { 
	
	@Autowired 
	private WebApplicationContext wac;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfileDao profileDao;
	
	private User testUser = TestUtility.testUser;
	
	private MockMvc mockMvc;

	Principal principal = new Principal() {
        @Override
        public String getName() {
            return "TEST_PRINCIPAL";
        }
    };
	
    @BeforeClass
    public static void baseSetUp() {
    	TestUtility.initialize();
    }
    
	@Before 
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		TestUtility.setUp(userDao, profileDao);
	} 

	@Test
	public void ValidSignUpTest() throws Exception {
		this.mockMvc
		.perform(
				post("/create").param("email", "test@test.test")
				.param("firstName", "test")
				.param("lastName", "test")
				.param("password", "testtest")
				.param("confirmPassword", "testtest")
				.param("tutor", "true"))
					.andExpect(status().is3xxRedirection())
					.andExpect(model().hasNoErrors());
		}
	
	@Test
	public void InvalidEmail() throws AssertionError, java.lang.Exception {
		this.mockMvc
		.perform(
				post("/create").param("email", "invalidEmail")
				.param("firstName", "test")
				.param("lastName", "test")
				.param("password", "testtest")
				.param("confirmPassword", "testtest")
				.param("tutor", "true"))
			.andExpect(status().is3xxRedirection())
			// TODO Rework this test
			//.andExpect(model().hasErrors())
			//.andExpect(model().errorCount(1));
		
;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void NullFirstName() throws Exception {
		this.mockMvc
		.perform(
				post("/create").param("email", "test@test.test")
				.param(null, "test")
				.param("lastName", "test")
				.param("password", "testtest")
				.param("confirmPassword", "testtest")
				.param("tutor", "true"));
		
	}
	
	@Test
	public void RegisterMapping() throws Exception {
		this.mockMvc
		.perform(
				get("/register")).andExpect(status().is3xxRedirection());
	}
	 
	@Test
	public void MappingHomeTest() throws Exception{
		
		this.mockMvc.perform(
				post("/")).andExpect(status().is3xxRedirection())
		.andExpect(model().hasNoErrors());
		
		this.mockMvc.perform(
				post("/home")).andExpect(status().is3xxRedirection())
						.andExpect(model().hasNoErrors());
		
	}
	
	@Test
	public void MappingLogin() throws Exception{
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(model().hasNoErrors());
		mockMvc.perform(get("/login").principal(principal)).andExpect(status().is3xxRedirection()).andExpect(model().hasNoErrors());
		mockMvc.perform(get("/login").param("error", "")).andExpect(status().isOk()).andExpect(model().attributeExists("error"));
		mockMvc.perform(get("/login").param("logout", "")).andExpect(status().isOk()).andExpect(model().attributeExists("msg"));
		mockMvc.perform(get("/login").param("register", "")).andExpect(status().isOk()).andExpect(model().attributeExists("switch"));
		mockMvc.perform(get("/login").param("success", "")).andExpect(status().isOk()).andExpect(model().attributeExists("msg"));
	}
	
	//FIXME @Test
	public void logoutTest() throws Exception {
		mockMvc.perform(get("/")).andExpect(unauthenticated());
		mockMvc.perform(formLogin("/login").user(testUser.getEmail())).andReturn();
		mockMvc.perform(logout()).andExpect(unauthenticated());
	}
	
}