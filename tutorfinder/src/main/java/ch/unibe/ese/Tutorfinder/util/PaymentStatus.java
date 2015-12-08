package ch.unibe.ese.Tutorfinder.util;

import ch.unibe.ese.Tutorfinder.controller.exceptions.PaymentStatusException;

/**
 * Enumeration of the Payment Status of a bill
 *
 *@version 2.0
 */
public enum PaymentStatus {

	/**
	 * The singleton instance for an unpaid bill.
	 * This has the numeric value of {@code 0}.
	 */
	UNPAID,
	
	/**
	 * The singleton instance for an paid bill.
	 * This has the numeric value of {@code 1}.
	 */
	PAID;
	
	/**
     * Private cache of all the constants.
     */
 
	private static final PaymentStatus[] ENUMS = PaymentStatus.values();
	
	public static PaymentStatus of(int paymentStatus) {
		if(paymentStatus < 0 || paymentStatus > 1)
			throw new PaymentStatusException("Invalid value for PaymentStatus: " + paymentStatus);
		
		return ENUMS[paymentStatus];
	}

    /**
     * Gets the paymentStatus {@code int} value.
     * <p>
     * The values are numbered as follow: 0 (unpaid), 1 (paid).
     *
     * @return the availability, from 0 (unpaid) to 1 (paid)
     */
    public int getValue() {
        return ordinal();
    }


}


