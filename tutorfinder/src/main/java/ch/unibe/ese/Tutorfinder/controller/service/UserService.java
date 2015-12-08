package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import ch.unibe.ese.Tutorfinder.model.User;

/**
 * @version	2.0
 */
public interface UserService {
	
	/**
	 * takes a {@link Principal} user and returns the actual user object
	 * @param user {@link Principal}
	 * @return User obj. that belongs to given {@link Principal}
	 */
	public User getUserByPrincipal(Principal user);
	
	/**
	 * takes an long value id and returns the user with that id
	 * @param id of the user that should be returned
	 * @return user with the given id
	 */
	public User getUserById(Long id);

	/**
	 * takes user and saves it in the database
	 * @param user that should be saved in the database
	 * @return user that was given as parameter
	 */
	public User save(User user);

	/**
	 * takes a {@link user} and changes it {@code role} to {@code Tutor}
	 * 
	 * @param user which wants to become a tutor
	 * @return user, which is now an tutor
	 */
	public User changeToTutor(User user);
}
