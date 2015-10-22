package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.model.User;

public interface FindTutorService {
	
	public Iterable<User> getUsersBySubject(String subject);

}
