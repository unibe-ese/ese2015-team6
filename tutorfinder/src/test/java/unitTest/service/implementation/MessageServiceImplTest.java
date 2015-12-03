package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.controller.service.MessageService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.controller.service.implementations.MessageServiceImpl;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.MessageDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class MessageServiceImplTest {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private MessageService messageService;
	
	@Mock
	private Message mockMessage;
	@Mock
	private User mockUser;
	@Mock
	private User mockSender;
	@Mock
	private Principal MockPrincipal;
	@Mock
	private UserService mockUserService;
	
	private MessageForm messageForm = new MessageForm();
	
	private List<Message> messageList = new ArrayList<Message>();
	
	
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
		
		this.messageForm.setMessage("This is a Message");
		this.messageForm.setSubject("This is a Subject");
		this.messageForm.setReceiver(this.mockUser);
		this.messageForm.setReceiverId(Long.valueOf(1));

		this.messageList.add(this.mockMessage);
		
	}
	
	@Test
	public void testGetMessageByInbox() {
		when(messageDao.findByReceiverOrderByTimestampDesc(mockUser)).thenReturn(this.messageList);
		
		List<Message> tmpMessageList = messageService.getMessageByBox(ConstantVariables.INBOX, this.mockUser);
		
		assertEquals(this.messageList, tmpMessageList);
	}
	
	@Test
	public void testGetMessageByOutbox() {
		when(messageDao.findBySenderOrderByTimestampDesc(mockUser)).thenReturn(this.messageList);
		
		List<Message> tmpMessageList = messageService.getMessageByBox(ConstantVariables.OUTBOX, this.mockUser);
		
		assertEquals(this.messageList, tmpMessageList);
	}
	
	@Test
	public void testGetMessageByBoxDefault() {
		when(messageDao.findByReceiverAndIsReadOrderByTimestampDesc(mockUser, false)).thenReturn(this.messageList);
		
		List<Message> tmpMessageList = messageService.getMessageByBox(ConstantVariables.UNREAD, this.mockUser);
		
		assertEquals(this.messageList, tmpMessageList);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetMessageByNullBox() {
		messageService.getMessageByBox(null, this.mockUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetMessageByEmptyBox() {
		messageService.getMessageByBox("", this.mockUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetMessageByBoxAndNullUser() {
		messageService.getMessageByBox(ConstantVariables.UNREAD, null);
	}
	
	@Test
	public void testMarkMessageAsRead() {
		when(messageDao.findOne(anyLong())).thenReturn(this.mockMessage);
		when(mockMessage.getReceiver()).thenReturn(this.mockUser);
		when(messageDao.save(mockMessage)).thenReturn(this.mockMessage);
		
		Message tmpMessage = messageService.markMessageAsRead(Long.valueOf(1), this.mockUser);
		
		verify(mockMessage).setIsRead(true);
		verify(messageDao).save(eq(mockMessage));
		assertEquals(this.mockMessage, tmpMessage);
	}
	
	@Test
	public void testMarkMessageAsReadWhenNotReceiver() {
		when(messageDao.findOne(anyLong())).thenReturn(this.mockMessage);
		when(mockMessage.getReceiver()).thenReturn(this.mockUser);
		
		Message tmpMessage = messageService.markMessageAsRead(Long.valueOf(1), this.mockSender);
		
		assertEquals(this.mockMessage, tmpMessage);
	}
	
	@Test(expected=AssertionError.class)
	public void testMarkMessageAsReadWhenNoMessageExist() {
		when(messageDao.findOne(anyLong())).thenReturn(null);
		
		messageService.markMessageAsRead(Long.valueOf(1), this.mockUser);
	}
	
	@Test(expected=AssertionError.class)
	public void testMarkMessageAsReadWhenNullUser() {
		messageService.markMessageAsRead(Long.valueOf(1), null);
	}
	
	@Test(expected=AssertionError.class)
	public void testMarkMessageAsReadWhenNullId() {
		messageService.markMessageAsRead(null, this.mockUser);
	}
	
	@After
	public void reset() {
		Mockito.reset(messageDao);
	}
	
	@Test
	public void testSaveFrom() {
		this.messageService = new MessageServiceImpl(this.messageDao, this.mockUserService);

		when(messageDao.save(any(Message.class))).thenReturn(this.mockMessage);
		when(mockUserService.getUserById(anyLong())).thenReturn(this.mockUser);
		when(mockUserService.getUserByPrincipal(eq(MockPrincipal))).thenReturn(this.mockSender);
		
		Message tmpMessage = messageService.saveFrom(this.messageForm, this.MockPrincipal);
		
		Message verifyMessage = new Message();
		verifyMessage.setMessage(this.messageForm.getMessage());
		verifyMessage.setSubject(this.messageForm.getSubject());
		verifyMessage.setReceiver(this.mockUser);
		verifyMessage.setSender(this.mockSender);
		verifyMessage.setTimestamp(new Timestamp((new Date()).getTime()));
		
		verify(messageDao).save(verifyMessage);
		assertEquals(this.mockMessage, tmpMessage);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testSaveFromWhenFormIsNull() {
		messageService.saveFrom(null, this.MockPrincipal);
	}
	
	@Test(expected=AssertionError.class)
	public void testSaveFromWhenPrincipalIsNull() {
		messageService.saveFrom(this.messageForm, null);
	}
	
}
