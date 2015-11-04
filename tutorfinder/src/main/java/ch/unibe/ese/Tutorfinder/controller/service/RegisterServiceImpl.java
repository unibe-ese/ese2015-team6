package ch.unibe.ese.Tutorfinder.controller.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;


/**
 * Service to save the information from the {@code SignupForm}
 * to the user-table on the database. This service is used for
 * create a new user.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	UserDao userDao;

	public RegisterServiceImpl() {
	}

	@Transactional
	public SignupForm saveFrom(SignupForm signupForm) throws InvalidUserException {

		User user = new User();
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		
		//checks whether input email address is already in the database
		if(userDao.findByEmail(signupForm.getEmail()) == null){ 
		user.setEmail(signupForm.getEmail());				
		} else {
			throw new InvalidEmailException("Email address already used");
		}

		user.setPassword(signupForm.getPassword());
		
		if (signupForm.isTutor()) {
			user.setRole(ConstantVariables.TUTOR);
		} else {
			user.setRole(ConstantVariables.STUDENT);
		}
		//adds an profile to the tutor connected by email
		Profile profile = new Profile(user.getEmail());
		profile.setWage(BigDecimal.ZERO);
		user.setProfile(profile);

		user = userDao.save(user); // save object to DB

		signupForm.setId(user.getId());

		return signupForm;
	}

}
