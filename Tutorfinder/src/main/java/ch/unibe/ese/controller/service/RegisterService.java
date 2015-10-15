package ch.unibe.ese.controller.service;

import ch.unibe.ese.controller.exceptions.InvalidUserException;
import ch.unibe.ese.controller.pojos.SignupForm;

public interface RegisterService {

	 public SignupForm saveFrom(SignupForm signupForm) throws InvalidUserException;
}
