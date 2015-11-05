package ch.unibe.ese.Tutorfinder.controller.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;

@Service
public interface SubjectService {
	
	public ArrayList<Subject> getAllSubjectsByUser(User user);

}
