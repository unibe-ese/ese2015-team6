package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

public interface SubjectDao extends CrudRepository<Subject, Long> {
	
	ArrayList<Subject> findAllByUser (User user);
}
