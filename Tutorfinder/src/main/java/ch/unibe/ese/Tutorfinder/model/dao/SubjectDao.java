package ch.unibe.ese.Tutorfinder.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Subject;

public interface SubjectDao extends CrudRepository<Subject, Long> {

}
