package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	public UserServiceImpl() {
	}
	
	@Override
	public User getUserByPrincipal(Principal user) {
		assert(user != null);
		
		User tmpUser = userDao.findByEmail(user.getName());
		assert(tmpUser != null);
		
		return tmpUser;
	}

	@Override
	public User getUserById(Long id) {
		assert(id != null);
		
		User tmpUser = userDao.findById(id);
		assert(tmpUser != null);
		
		return tmpUser;
	}

	@Override
	public User save(User user) {
		assert (user != null);
		user = userDao.save(user);
		assert (user != null);
		return user;
	}

	@Override
	public User changeToTutor(User user) {
		assert (user != null);
		
		if(!user.getRole().equals(ConstantVariables.TUTOR)){
			user.setRole(ConstantVariables.TUTOR);
			return userDao.save(user);
		}

		return user;
	}

}
