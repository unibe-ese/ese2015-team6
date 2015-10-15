package ch.unibe.ese.Tutorfinder.model.dao;

import org.springframework.data.repository.CrudRepository;
import ch.unibe.ese.Tutorfinder.model.User;

public interface UserDao extends CrudRepository<User,Long>{

}
