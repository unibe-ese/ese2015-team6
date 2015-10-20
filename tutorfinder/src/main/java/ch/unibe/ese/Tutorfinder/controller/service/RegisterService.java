package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.SignupForm;

public interface RegisterService {

	 public SignupForm saveFrom(SignupForm signupForm) throws InvalidUserException;
}
