
package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;

public interface RegisterService {

	 public SignupForm saveFrom(SignupForm signupForm) throws InvalidEmailException;
}

