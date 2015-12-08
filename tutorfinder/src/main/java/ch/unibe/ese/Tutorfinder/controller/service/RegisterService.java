
package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;


/**
 * @version	2.0
 */
public interface RegisterService {

	/**
	 * takes {@link signupForm} and creates a new User in the database
	 * @param {@link signupForm}
	 * @return the same {@link signupForm}}
	 * @throws InvalidEmailException is thrown when their is already a database entry
	 * with the sam email as the one in the {@link signupForm}
	 */
	 public SignupForm saveFrom(SignupForm signupForm) throws InvalidEmailException;
}

