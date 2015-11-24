package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.List;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MessageForm;
import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;

public interface MessageService {

	/**
	 * Returns a list of {@link Message}s for the {@code authUser}
	 * corresponding to the selected {@code Box}
	 * 
	 * @param box value for the messages which should be given ("inbox" or "outbox", "trash")
	 * @param authUser {@link User} who's actual logged in and whose messages need to be shown
	 * @return {@link List} of {@link Message}s for a given box and user
	 */
	public List<Message> getMessageByBox(String box, User authUser);
	
	/**
	 * Removes the {@link Message} from inbox or outbox into the trash box 
	 * by changing the {@code senderDeleted} or {@code receiverDeleted} to {@code true}.
	 * 
	 * @param messageId to identify which message should be modified
	 * @param authUser {@link User} who's actual logged in and whose messages need to be modified
	 * @return Message which has been modified
	 */
	public Message deleteMessage(Long messageId, User authUser);
	
	/**
	 * Saves a {@link Message} into the database. After this method
	 * has been called, the message is displayed in the {@code senders} 
	 * outbox and in the {@code receivers} inbox.
	 * 
	 * @param messageForm holds all parameter for creating a new message
	 * @return the new created and saved message
	 */
	public Message saveMessage(MessageForm messageForm);
}
