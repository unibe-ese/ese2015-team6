package ch.unibe.ese.Tutorfinder.controller.service;

import java.math.BigDecimal;
import java.util.List;

import ch.unibe.ese.Tutorfinder.model.Bill;
import ch.unibe.ese.Tutorfinder.model.User;

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
	public List<Bill> getBills(User user); 
	
	/**
	 * updates the bills for the previous month
	 */
	public void updateMonthlyBills();

}
