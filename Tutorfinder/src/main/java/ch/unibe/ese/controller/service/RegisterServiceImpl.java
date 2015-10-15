package ch.unibe.ese.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.controller.exceptions.InvalidUserException;
import ch.unibe.ese.controller.pojos.SignupForm;

public class RegisterServiceImpl implements RegisterService {

	 @Autowired    UserDao userDao;
	
	public RegisterServiceImpl()  {}
	
	
	@Transactional
	public SignupForm saveFrom(SignupForm signupForm) throws InvalidUserException {
		
		User user = new User();
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setEmail(signupForm.getEmail());
		user.setPassword(signupForm.getPassword());
		
		user = userDao.save(user);   // save object to DB
		
		signupForm.setId(user.getId());

        return signupForm;
	}

}
