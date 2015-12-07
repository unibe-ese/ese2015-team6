package integrationTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
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

import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.dao.MessageDao;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 

@Transactional 
public class MessageControllerTest {

	@Autowired private WebApplicationContext wac; 
	@Autowired private UserDao userDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private MessageDao messageDao;

	private MockMvc mockMvc;
	private Principal authUser;
	private Principal authUserTwo;


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
		this.authUserTwo = TestUtility.createPrincipal(TestUtility.testUserTwo.getEmail(), "password", "ROLE_TUTOR");

	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessages() throws Exception {
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:messages?view=" + ConstantVariables.UNREAD));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesUnread() throws Exception {
		List<Message> emptyMessageList = new ArrayList<Message>();
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.UNREAD))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("messagesOverview"))
			.andExpect(model().attribute("messageList", emptyMessageList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesInbox() throws Exception {
		List<Message> emptyMessageList = new ArrayList<Message>();
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.INBOX))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("messagesOverview"))
			.andExpect(model().attribute("messageList", emptyMessageList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesOutbox() throws Exception {
		List<Message> emptyMessageList = new ArrayList<Message>();
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.OUTBOX))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("messagesOverview"))
			.andExpect(model().attribute("messageList", emptyMessageList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesOutboxWithShow() throws Exception {
		String receiverId = "" + TestUtility.testUserTwo.getId();
		this.mockMvc
			.perform(
					post("/sendMessage").principal(this.authUser)
					.param("receiverId", receiverId)
					.param("subject", "TestSubject")
					.param("message", "TestMessage"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("messages?view=" + ConstantVariables.OUTBOX + "&show=0"))
			.andExpect(view().name("redirect:messages?view=" + ConstantVariables.OUTBOX + "&show=0"));
		
		List<Message> tmpMessageList = messageDao.findByReceiverOrderByTimestampDesc(TestUtility.testUserTwo);
		
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.OUTBOX)
				.param("show", "0"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("messagesOverview"))
			.andExpect(model().attribute("messageList", tmpMessageList));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesOutboxWithShowNotExists() throws Exception {
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.OUTBOX)
				.param("show", "0"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:messages?view=" + ConstantVariables.OUTBOX))
			.andExpect(redirectedUrl("messages?view=" + ConstantVariables.OUTBOX));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesOutboxWithWrongShow() throws Exception {
		String receiverId = "" + TestUtility.testUserTwo.getId();
		this.mockMvc
			.perform(
					post("/sendMessage").principal(this.authUser)
					.param("receiverId", receiverId)
					.param("subject", "TestSubject")
					.param("message", "TestMessage"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("messages?view=" + ConstantVariables.OUTBOX + "&show=0"))
			.andExpect(view().name("redirect:messages?view=" + ConstantVariables.OUTBOX + "&show=0"));
		
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.OUTBOX)
				.param("show", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:messages?view=" + ConstantVariables.OUTBOX + "&show=0"))
			.andExpect(redirectedUrl("messages?view=" + ConstantVariables.OUTBOX + "&show=0"));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testMessagesInboxWithShowMarkedAsWatch() throws Exception {
		String receiverId = "" + TestUtility.testUser.getId();
		this.mockMvc
			.perform(
					post("/sendMessage").principal(this.authUserTwo)
					.param("receiverId", receiverId)
					.param("subject", "TestSubject")
					.param("message", "TestMessage"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("messages?view=" + ConstantVariables.OUTBOX + "&show=0"))
			.andExpect(view().name("redirect:messages?view=" + ConstantVariables.OUTBOX + "&show=0"));
		
		this.mockMvc
			.perform(
					get("/messages").principal(this.authUser)
				.param("view", ConstantVariables.INBOX)
				.param("show", "0"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(model().attributeExists("messageList"))
			.andExpect(view().name("messagesOverview"));
		
		List<Message> tmpMessageList = messageDao.findByReceiverOrderByTimestampDesc(TestUtility.testUser);
		
		assertFalse(tmpMessageList.isEmpty());
		
		Message tmpMessage = tmpMessageList.get(0);
		 
		assertEquals("TestMessage", tmpMessage.getMessage());
		assertEquals("TestSubject", tmpMessage.getSubject());
		assertEquals(TestUtility.testUserTwo, tmpMessage.getSender());
		assertEquals(TestUtility.testUser, tmpMessage.getReceiver());
		assertTrue(tmpMessage.isRead());
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testNewMessage() throws Exception {
		String receiverId = "" + TestUtility.testUserTwo.getId();
		this.mockMvc
			.perform(
					get("/newMessage").principal(this.authUser)
					.param("receiver", receiverId))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("newMessage"))
			.andExpect(model().attributeExists("messageForm"));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testNewMessageWhenUserDoesNotExist() throws Exception {
		this.mockMvc
			.perform(
					get("/newMessage").principal(this.authUser)
					.param("receiver", "0"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:findTutor"));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testNewMessageToMySelf() throws Exception {
		String receiverId = "" + TestUtility.testUser.getId();
		this.mockMvc
			.perform(
					get("/newMessage").principal(this.authUser)
					.param("receiver", receiverId))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("findTutor"))
			.andExpect(view().name("redirect:findTutor"));
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testSendMessage() throws Exception {
		String receiverId = "" + TestUtility.testUserTwo.getId();
		this.mockMvc
			.perform(
					post("/sendMessage").principal(this.authUser)
					.param("receiverId", receiverId)
					.param("subject", "TestSubject")
					.param("message", "TestMessage"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("messages?view=outbox&show=0"))
			.andExpect(view().name("redirect:messages?view=outbox&show=0"));

		List<Message> tmpMessageList = messageDao.findByReceiverOrderByTimestampDesc(TestUtility.testUserTwo);
		
		assertFalse(tmpMessageList.isEmpty());
		
		Message tmpMessage = tmpMessageList.get(0);
		 
		assertEquals("TestMessage", tmpMessage.getMessage());
		assertEquals("TestSubject", tmpMessage.getSubject());
		assertEquals(TestUtility.testUser, tmpMessage.getSender());
		assertEquals(TestUtility.testUserTwo, tmpMessage.getReceiver());
		assertFalse(tmpMessage.isRead());
	}
	
	@Test
	@WithMockUser(roles="TUTOR")
	public void testSendMessageWithErrors() throws Exception {
		String receiverId = "" + TestUtility.testUserTwo.getId();
		
		this.mockMvc
			.perform(
					post("/sendMessage").principal(this.authUser)
					.param("receiverId", receiverId)
					.param("subject", "")
					.param("message", "TestMessage"))
			.andExpect(model().attributeHasErrors("messageForm"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("newMessage"));
	}
	
}
