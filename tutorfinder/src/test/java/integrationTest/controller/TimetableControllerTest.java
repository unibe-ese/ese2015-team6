package integrationTest.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
import java.time.DayOfWeek;
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

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 
@Transactional 
public class TimetableControllerTest {


		@Autowired private WebApplicationContext wac; 
		@Autowired private UserDao userDao;
		@Autowired private ProfileDao profileDao;
		@Autowired private TimetableDao timetableDao;

		private MockMvc mockMvc;
		private Principal authUser;
		
		private Boolean[][] timetable = new Boolean[ConstantVariables.TIMESLOTS][ConstantVariables.DAYS];
		private UpdateTimetableForm updateTimetableForm = new UpdateTimetableForm();


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
			this.timetable[0][0] = true;
			this.updateTimetableForm.setTimetable(this.timetable);

		}
		
		@Test
		@WithMockUser(roles="TUTOR")
		public void testUpdateTimetable() throws Exception {
			this.timetable[0][0] = true;
			
			this.mockMvc
				.perform(
						post("/updateTimetable").principal(this.authUser)
						.param("timetable[0][0]", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/editProfile"))
				.andExpect(flash().attributeExists("timetable_msg"))
				.andExpect(redirectedUrl("/editProfile"));
			
			List<Timetable> tmpTimetable = timetableDao.findAllByUser(TestUtility.testUser);
			
			assertFalse(tmpTimetable.isEmpty());
			assertEquals(1, tmpTimetable.size());
			Timetable timetable = tmpTimetable.get(0);
			
			assertEquals(TestUtility.testUser, timetable.getUser());
			assertEquals(DayOfWeek.of(1), timetable.getDay());
			assertEquals(0 , timetable.getTime());
			
		}
		
		@Test
		@WithMockUser(roles="TUTOR")
		public void testUpdateTimetableWithErrors() throws Exception {
			this.timetable[0][0] = true;
			
			this.mockMvc
				.perform(
						post("/updateTimetable").principal(this.authUser)
						.param("timetable", "null"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/editProfile"))
				.andExpect(flash().attributeExists("updateTimetableForm"))
				.andExpect(redirectedUrl("/editProfile"));
			
			List<Timetable> tmpTimetable = timetableDao.findAllByUser(TestUtility.testUser);
			
			assertTrue(tmpTimetable.isEmpty());
			
		}
		
}