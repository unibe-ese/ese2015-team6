package integrationTest.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.File;
import java.security.Principal;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
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
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 

@Transactional 
public class PictureControllerTest {
	
	@Autowired private WebApplicationContext wac; 
	@Autowired private UserDao userDao;
	@Autowired private ProfileDao profileDao;

	private MockMvc mockMvc;
	private Principal authUser;

	//Clean up the temporarily folder
	@AfterClass
	public static void cleanUp() {
		FileUtils.deleteQuietly(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator 
				+ "test" + File.separator + "src" + File.separator));
	}

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
	public void testUploadImageWhenFolderDoesNotExist() throws Exception {
		String rootPath = System.getProperty("user.dir");
		try {
			System.setProperty("user.dir", rootPath + File.separator + "src" + File.separator + "test" + File.separator);
			MockMultipartFile file = new MockMultipartFile("file", "original_filename.ext", null, "data".getBytes());
			
			this.mockMvc
				.perform(
						fileUpload("/uploadImage")
						.file(file)
						.principal(this.authUser))
				.andExpect(status().isOk())
				.andExpect(view().name("updateProfile"));
			
			File tmpFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
					+ "webapp" + File.separator + "img" + File.separator + "profPic" + File.separator 
					+ TestUtility.testUser.getId() + ".png");
			
			assertFalse(tmpFile.isDirectory());
			assertTrue(tmpFile.exists());
		} finally {
			System.setProperty("user.dir", rootPath);
		}
		
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testUploadImageWhenFolderExist() throws Exception {
		String rootPath = System.getProperty("user.dir");
		try {
			System.setProperty("user.dir", rootPath + File.separator + "src" + File.separator + "test" + File.separator);
			MockMultipartFile file = new MockMultipartFile("file", "original_filename.ext", null, "data".getBytes());
			
			this.mockMvc
				.perform(
						fileUpload("/uploadImage")
						.file(file)
						.principal(this.authUser))
				.andExpect(status().isOk())
				.andExpect(view().name("updateProfile"));
			
			assertTrue(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
					+ "webapp" + File.separator + "img" + File.separator + "profPic").isDirectory());
			
			this.mockMvc
			.perform(
					fileUpload("/uploadImage")
					.file(file)
					.principal(this.authUser))
			.andExpect(status().isOk())
			.andExpect(view().name("updateProfile"));
			
			File tmpFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
					+ "webapp" + File.separator + "img" + File.separator + "profPic" + File.separator 
					+ TestUtility.testUser.getId() + ".png");
			
			assertFalse(tmpFile.isDirectory());
			assertTrue(tmpFile.exists());
		} finally {
			System.setProperty("user.dir", rootPath);
		}
		
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testUploadImageWhenEmpty() throws Exception {
		String rootPath = System.getProperty("user.dir");
		try {
			System.setProperty("user.dir", rootPath + File.separator + "src" + File.separator + "test" + File.separator);
			MockMultipartFile file = new MockMultipartFile("file", "original_filename.ext", null, "".getBytes());
			
			this.mockMvc
				.perform(
						fileUpload("/uploadImage")
						.file(file)
						.principal(this.authUser))
				.andExpect(status().isOk())
				.andExpect(view().name("updateProfile"));
			
			File tmpFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
					+ "webapp" + File.separator + "img" + File.separator + "profPic" + File.separator 
					+ TestUtility.testUser.getId() + ".png");
			
			assertFalse(tmpFile.isDirectory());
			assertFalse(tmpFile.exists());
			
		} finally {
			System.setProperty("user.dir", rootPath);
		}
		
	}
}
