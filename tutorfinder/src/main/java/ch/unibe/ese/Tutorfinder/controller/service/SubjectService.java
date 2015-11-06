package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.ArrayList;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;


public interface SubjectService {
	
	public ArrayList<Subject> getAllSubjectsByUser(User user);
	
	public UpdateSubjectsForm saveFrom(UpdateSubjectsForm updateSubjectsForm, Principal user) throws InvalidSubjectException;

}
