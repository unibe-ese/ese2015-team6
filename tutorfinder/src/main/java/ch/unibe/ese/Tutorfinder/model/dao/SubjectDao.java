package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

public interface SubjectDao extends CrudRepository<Subject, Long> {

	Subject findById (long id);
	
	ArrayList<Subject> findAllById (long id);
	
	ArrayList<Subject> findAllByUser (User user);
	
	LinkedList<Subject> findByName(String name);
}
