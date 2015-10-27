package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateTimetableForm;

public interface UpdateTimetableService {

	public UpdateTimetableForm saveFrom(UpdateTimetableForm updateTimetableForm, Principal user) throws InvalidTimetableException;
}
