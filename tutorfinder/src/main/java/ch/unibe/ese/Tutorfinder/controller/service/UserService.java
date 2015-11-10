package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import ch.unibe.ese.Tutorfinder.model.User;

public interface UserService {
	
	public User getUserByPrincipal(Principal user);
	
	public User getUserById(Long id);

	public User save(User user);

}
