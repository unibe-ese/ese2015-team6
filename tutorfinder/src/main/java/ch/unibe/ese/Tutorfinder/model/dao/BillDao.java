package ch.unibe.ese.Tutorfinder.model.dao;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.Tutorfinder.model.Bill;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.PaymentStatus;

public interface BillDao extends CrudRepository<Bill, Long>{
	
	ArrayList<Bill> findAllByTutor (User tutor);
	
	ArrayList<Bill> findAllByTutorAndPaymentStatus (User tutor, PaymentStatus paymentStatus);

	Bill findById(long id);
}
