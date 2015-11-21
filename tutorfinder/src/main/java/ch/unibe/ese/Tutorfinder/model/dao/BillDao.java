package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Bill;
import ch.unibe.ese.Tutorfinder.model.User;

public interface BillDao extends CrudRepository<Bill, Long>{
	
	ArrayList<Bill> findAllByTutor (User tutor);

}
