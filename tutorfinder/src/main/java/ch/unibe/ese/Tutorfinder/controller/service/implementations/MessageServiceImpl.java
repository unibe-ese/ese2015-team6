package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.controller.service.MessageService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.MessageDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	@Autowired 
	private UserService userService;
	
	public MessageServiceImpl() {
		super();
	}

	/**
	 * Constructor for testing purposes
	 * 
	 * @param messageDao
	 * @param userService
	 */
	public MessageServiceImpl(MessageDao messageDao, UserService userService) {
		this.messageDao = messageDao;
		this.userService = userService;
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public List<Message> getMessageByBox(String box, User authUser) {
		assert (box != null && !box.isEmpty());
		assert (authUser != null);
		
		if(ConstantVariables.INBOX.equals(box)) {
			return messageDao.findByReceiverOrderByTimestampDesc(authUser);
		} else if (ConstantVariables.OUTBOX.equals(box)) {
			return messageDao.findBySenderOrderByTimestampDesc(authUser);
		} else {
			return messageDao.findByReceiverAndIsReadOrderByTimestampDesc(authUser, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message markMessageAsRead(Long messageId, User authUser) {
		assert (messageId != null);
		assert (authUser != null);
		
		Message tmpMessage = messageDao.findOne(messageId);
		assert (tmpMessage != null);
		
		if (tmpMessage.getReceiver().equals(authUser)) {
			tmpMessage.setIsRead(true);
			tmpMessage = messageDao.save(tmpMessage);
		}
		return tmpMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Message saveFrom(MessageForm messageForm, Principal authUser){
		assert (messageForm != null);
		assert (authUser != null);
		
		Message newMessage = new Message();
		
		newMessage.setReceiver(userService.getUserById(messageForm.getReceiverId()));
		newMessage.setSender(userService.getUserByPrincipal(authUser));
		newMessage.setTimestamp(new Timestamp((new Date()).getTime()));
		newMessage.setMessage(messageForm.getMessage());
		newMessage.setSubject(messageForm.getSubject());
		
		newMessage = messageDao.save(newMessage);
		
		return newMessage;
	}

}
