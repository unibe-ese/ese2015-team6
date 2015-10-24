package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

public interface SubjectDao extends CrudRepository<Subject, Long> {

	Subject findById (long id);
	
	Subject findAllById (long id);
	
	Subject findAllByUser (User user);
	
	LinkedList<Subject> findByName(String name);
}
