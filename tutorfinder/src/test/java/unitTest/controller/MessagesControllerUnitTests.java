package unitTest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.MessageController;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidMessageException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.controller.service.MessageService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml"})
public class MessagesControllerUnitTests {

	private MessageController controller;
	
	@Mock
	private MessageService mockMessageService;
	@Mock
	private UserService mockUserService;
	@Mock
	private HttpServletRequest mockReq;
	@Mock
	private BindingResult mockBindingResult;
	@Mock
	private RedirectAttributes mockRedirectAttributes;
	@Mock
	private Principal mockAuthUser;
	@Mock
	private User mockUser;
	@Mock
	private User mockOtherUser;
	@Mock
	private Message mockMessage;
	
	private MessageForm messageForm;
	
	private List<Message> messageList;
	
	@After
	public void reset() {
		Mockito.reset( mockMessageService );
	}
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
		
		this.mockMessage.setId(Long.valueOf(1));
		this.mockMessage.setIsRead(false);
		this.mockMessage.setMessage("This is a Message");
		this.mockMessage.setReceiver(this.mockUser);
		this.mockMessage.setSender(this.mockUser);
		this.mockMessage.setSubject("This is a Subject");
		this.mockMessage.setTimestamp(new Timestamp((new Date()).getTime()));
		
		messageForm = new MessageForm();
		this.messageForm.setMessage("This is a Message");
		this.messageForm.setSubject("This is a Subject");
		this.messageForm.setReceiver(this.mockUser);
		this.messageForm.setReceiverId(Long.valueOf(1));

		messageList = new ArrayList<Message>();
		this.messageList.add(this.mockMessage);
		
		this.controller = new MessageController(this.mockMessageService, this.mockUserService);
	}
	
	@Test
	public void testMessagesInbox() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.getMessageByBox(eq(ConstantVariables.INBOX), eq(mockUser))).thenReturn(this.messageList);
		
		ModelAndView gotMav = controller.messages(mockAuthUser, ConstantVariables.INBOX, null);
		
		verify(mockMessageService).getMessageByBox(eq(ConstantVariables.INBOX), eq(this.mockUser));
		assertEquals("messagesOverview", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageList"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
	}
	
	@Test
	public void testMessagesOutbox() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.getMessageByBox(eq(ConstantVariables.OUTBOX), eq(mockUser))).thenReturn(this.messageList);
		
		ModelAndView gotMav = controller.messages(mockAuthUser, ConstantVariables.OUTBOX, null);
		
		verify(mockMessageService).getMessageByBox(eq(ConstantVariables.OUTBOX), eq(this.mockUser));
		assertEquals("messagesOverview", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageList"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
	}
	
	@Test
	public void testMessagesUnreadBox() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.getMessageByBox(eq(ConstantVariables.UNREAD), eq(mockUser))).thenReturn(this.messageList);
		
		ModelAndView gotMav = controller.messages(mockAuthUser, ConstantVariables.UNREAD, null);
		
		verify(mockMessageService).getMessageByBox(eq(ConstantVariables.UNREAD), eq(this.mockUser));
		assertEquals("messagesOverview", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageList"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
	}
	
	@Test
	public void testMessagesDefaultBox() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.getMessageByBox(eq(ConstantVariables.UNREAD), eq(mockUser))).thenReturn(this.messageList);
		
		ModelAndView gotMav = controller.messages(mockAuthUser, null, null);
		
		assertEquals("redirect:messages?view="+ ConstantVariables.UNREAD, gotMav.getViewName());
	}
	
	@Test
	public void testMessagesWithShow() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.getMessageByBox(eq(ConstantVariables.UNREAD), eq(mockUser))).thenReturn(this.messageList);
		when(mockMessage.getReceiver()).thenReturn(this.mockUser);
		when(mockMessage.getId()).thenReturn(Long.valueOf(0));
		
		ModelAndView gotMav = controller.messages(mockAuthUser, ConstantVariables.UNREAD, Long.valueOf(0));

		verify(mockMessageService).getMessageByBox(eq(ConstantVariables.UNREAD), eq(this.mockUser));
		verify(mockMessageService).markMessageAsRead(Long.valueOf(0), this.mockUser);
		assertEquals("messagesOverview", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageList"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
	}
	
	@Test
	public void testMessagesWithShowButNotReceiver() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.getMessageByBox(eq(ConstantVariables.UNREAD), eq(mockUser))).thenReturn(this.messageList);
		when(mockMessage.getReceiver()).thenReturn(this.mockOtherUser);
		when(mockMessage.getId()).thenReturn(Long.valueOf(0));
		
		ModelAndView gotMav = controller.messages(mockAuthUser, ConstantVariables.UNREAD, Long.valueOf(0));

		verify(mockMessageService).getMessageByBox(eq(ConstantVariables.UNREAD), eq(this.mockUser));
		assertEquals("messagesOverview", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageList"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
	}
	
	@Test
	public void testNewMessage() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockUserService.getUserById(anyLong())).thenReturn(this.mockOtherUser);
		when(mockReq.getParameter(anyString())).thenReturn("0");
		when(mockUser.getId()).thenReturn(Long.valueOf(1));
		
		ModelAndView gotMav = controller.newMessage(this.mockAuthUser, this.mockReq);
		
		assertEquals("newMessage", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageForm"));
	}
	
	@Test
	public void testNewMessageForMySelf() {
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockUserService.getUserById(anyLong())).thenReturn(this.mockOtherUser);
		when(mockReq.getParameter(anyString())).thenReturn("0");
		when(mockUser.getId()).thenReturn(Long.valueOf(0));
		
		ModelAndView gotMav = controller.newMessage(this.mockAuthUser, this.mockReq);
		
		assertEquals("redirect:findTutor", gotMav.getViewName());
	}
	
	@Test
	public void testSendMessage() {
		when(mockBindingResult.hasErrors()).thenReturn(false);
		
		ModelAndView gotMav = controller.sendMessage(this.mockAuthUser, this.messageForm, this.mockBindingResult, this.mockRedirectAttributes);
		
		verify(mockMessageService).saveFrom(eq(messageForm), eq(mockAuthUser));
		assertEquals("redirect:messages?view="+ ConstantVariables.OUTBOX+"&show=0", gotMav.getViewName());
	}
	
	@Test
	public void testSendMessageWhenHasErrors() {
		when(mockBindingResult.hasErrors()).thenReturn(true);
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		
		ModelAndView gotMav = controller.sendMessage(this.mockAuthUser, this.messageForm, this.mockBindingResult, this.mockRedirectAttributes);
		
		assertEquals("newMessage", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageForm"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
		assertEquals(this.mockUser, gotMav.getModel().get("authUser"));
		assertEquals(this.messageForm, gotMav.getModel().get("messageForm"));
		
	}
	
	@Test
	public void testSendMessageWhenInvalidMessageException() {
		when(mockBindingResult.hasErrors()).thenReturn(false);
		when(mockUserService.getUserByPrincipal(mockAuthUser)).thenReturn(this.mockUser);
		when(mockMessageService.saveFrom(any(MessageForm.class), eq(mockAuthUser))).thenThrow(new InvalidMessageException("Test Exception"));
		
		ModelAndView gotMav = controller.sendMessage(this.mockAuthUser, this.messageForm, this.mockBindingResult, this.mockRedirectAttributes);
		
		verify(mockMessageService).saveFrom(eq(messageForm), eq(mockAuthUser));
		assertEquals("newMessage", gotMav.getViewName());
		assertTrue(gotMav.getModel().containsKey("messageForm"));
		assertTrue(gotMav.getModel().containsKey("authUser"));
		assertTrue(gotMav.getModel().containsKey("page_error"));
		assertEquals(this.mockUser, gotMav.getModel().get("authUser"));
		assertEquals(this.messageForm, gotMav.getModel().get("messageForm"));
		assertEquals("Test Exception", gotMav.getModel().get("page_error"));
	}
	
	
	
}
