package ch.unibe.ese.Tutorfinder.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Message;

public interface MessageDao extends CrudRepository<Message, Long> {

}
