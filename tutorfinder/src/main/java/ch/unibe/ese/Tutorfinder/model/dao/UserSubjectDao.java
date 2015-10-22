package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.UserSubject;

public interface UserSubjectDao extends CrudRepository<UserSubject, Long> {

		LinkedList<UserSubject> findBySubject(String subject);
}
