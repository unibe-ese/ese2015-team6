package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Message;
import ch.unibe.ese.Tutorfinder.model.User;

public interface MessageDao extends CrudRepository<Message, Long> {

	List<Message> findByReceiverOrderByTimestampDesc(User receiver);

	List<Message> findBySenderOrderByTimestampDesc(User sender);

	List<Message> findByReceiverAndIsReadOrderByTimestampDesc(User receiver, boolean isRead);

}
