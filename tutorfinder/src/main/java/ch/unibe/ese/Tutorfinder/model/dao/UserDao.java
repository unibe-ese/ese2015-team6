package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.User;


public interface UserDao extends CrudRepository<User,Long>{
	
	User findByEmail(String emailAddress);
	
	User findById(long id);
	
	List<User> findAllByRole(String role);

}
