package integrationTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
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

import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 
@Transactional 
public class SubjectControllerTest {



	@Autowired private WebApplicationContext wac; 
	@Autowired private UserDao userDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private SubjectDao subjectDao;

	private MockMvc mockMvc;
	private Principal authUser;
	


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

	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testEditSubjects() throws Exception {
		
		this.mockMvc
			.perform(
					post("/editSubjects").principal(this.authUser)
					.param("save", "true")
					.param("rows[0].subject", "testSubject")
					.param("rows[0].grade", "5.0"))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().hasNoErrors())
			.andExpect(view().name("redirect:/editProfile"))
			.andExpect(flash().attributeExists("subject_msg"))
			.andExpect(flash().attributeExists("updateSubjectsForm"))
			.andExpect(redirectedUrl("/editProfile"));
		
		List<Subject> tmpSubjectList = subjectDao.findAllByUser(TestUtility.testUser);
		assertFalse(tmpSubjectList.isEmpty());
		assertEquals(1, tmpSubjectList.size());
		
		Subject subject = tmpSubjectList.get(0);
		
		assertEquals("testSubject", subject.getName());
		assertEquals(Double.valueOf(5.0), (Double) subject.getGrade());
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testEditSubjectsAddSameTwice() throws Exception {
		
		this.mockMvc
			.perform(
					post("/editSubjects").principal(this.authUser)
					.param("save", "true")
					.param("rows[0].subject", "testSubject")
					.param("rows[0].grade", "5.0")
					.param("rows[1].subject", "testSubject")
					.param("rows[1].grade", "5.0"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/editProfile"))
			.andExpect(flash().attributeExists("org.springframework.validation.BindingResult.updateSubjectsForm"))
			.andExpect(redirectedUrl("/editProfile"));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testEditSubjectsWhenHasErrors() throws Exception {
		
		this.mockMvc
			.perform(
					post("/editSubjects").principal(this.authUser)
					.param("save", "true")
					.param("rows", "null"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/editProfile"))
			.andExpect(redirectedUrl("/editProfile"));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testAddRow() throws Exception {
		
		this.mockMvc
			.perform(
					post("/editSubjects").principal(this.authUser)
					.param("addRow", "true")
					.param("rows[0].subject", "testSubject")
					.param("rows[0].grade", "5.0"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(model().hasNoErrors())
			.andExpect(view().name("updateProfile"))
			.andExpect(model().attributeExists("updateSubjectsForm"));
		
		assertEquals(0, subjectDao.count());
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testRemoveRow() throws Exception {
		this.mockMvc
			.perform(
					post("/editSubjects").principal(this.authUser)
					.param("remRow", "0")
					.param("rows[0].subject", "testSubject")
					.param("rows[0].grade", "5.0"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(model().hasNoErrors())
			.andExpect(view().name("updateProfile"))
			.andExpect(model().attributeExists("updateSubjectsForm"));
		
		assertEquals(0, subjectDao.count());
	}
	
}
