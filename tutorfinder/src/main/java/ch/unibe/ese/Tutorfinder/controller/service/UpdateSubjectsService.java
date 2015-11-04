package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;

public interface UpdateSubjectsService {
	
	public UpdateSubjectsForm saveFrom(UpdateSubjectsForm updateSubjectsForm, Principal user) throws InvalidSubjectException;

}
