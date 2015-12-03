package ch.unibe.ese.Tutorfinder.controller.service;

import java.math.BigDecimal;
import java.util.List;

import ch.unibe.ese.Tutorfinder.model.Bill;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.PaymentStatus;

public interface BillService {
	
	/**
	 * calculates the temporary, current billing amount 
	 * for a given {@link User}
	 * @param user for whom the billing amount should be calculated
	 * @return current billing amount 
	 */
	public	BigDecimal getBillForCurrentMonth(User user);
	
	/**
	 * Loads and returns all {@link Bill}s for a given {@link User} from the database
	 * @param user whose bills should be returned
	 * @return List of {@link Bill}s of the given {@link User}
	 */
	public List<Bill> getBills(User user, PaymentStatus paymentStatus); 
	
	/**
	 * updates the bills for the previous month
	 */
	public void updateMonthlyBills();

	/**
	 * takes the Id of an unpaid {@link Bill} and sets its payment status to true
	 * @param User must correspond to the user that is saved in the bill, cannot be null
	 * @param BillId id of the bill that should be paid
	 */
	public void pay(User user, long BillId);  

}
