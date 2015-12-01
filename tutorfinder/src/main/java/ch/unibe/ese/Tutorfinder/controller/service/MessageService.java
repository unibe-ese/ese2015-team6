package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * @version	1.0
 *
 */
public interface MessageService {

	/**
	 * Returns a list of {@link Message}s for the {@code authUser}
	 * corresponding to the selected {@code Box}
	 * 
	 * @param box value for the messages which should be given ("inbox" or "outbox", "unread")
	 * @param authUser {@link User} who's actual logged in and whose messages need to be shown
	 * @return {@link List} of {@link Message}s for a given box and user
	 */
	public List<Message> getMessageByBox(String box, User authUser);
	
	/**
	 * Removes the {@link Message} from unread box 
	 * by changing the {@code isRead} to true.
	 * 
	 * @param messageId to identify which message should be modified
	 * @param authUser {@link User} who's actual logged in and whose messages need to be modified
	 * @return Message which has been modified
	 */
	public Message markMessageAsRead(Long messageId, User authUser);
	
	/**
	 * Saves a {@link Message} into the database. After this method
	 * has been called, the message is displayed in the {@code senders} 
	 * outbox and in the {@code receivers} inbox.
	 * 
	 * @param messageForm holds all parameter for creating a new message
	 * @param authuser, actual user, who wants to send a message
	 * @return the new created and saved message
	 */
	public Message saveFrom(MessageForm messageForm, Principal authUser);

	/**
	 * Search in the database for a message with the given id
	 * 
	 * @param id for searching the Message, not null
	 * @return Message with the given id
	 */
	public Message getMessageById(Long id);
}
