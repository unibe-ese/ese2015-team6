package ch.unibe.ese.Tutorfinder.model.dao;

import ch.unibe.ese.Tutorfinder.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserDao extends CrudRepository<User,Long>{
	
	User findByEmail(String emailAddress);

}
