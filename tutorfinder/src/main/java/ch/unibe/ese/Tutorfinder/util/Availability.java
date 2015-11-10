package ch.unibe.ese.Tutorfinder.util;

import ch.unibe.ese.Tutorfinder.controller.exceptions.AvailabilityException;

/**
 * Enumeration for the availability of an Timetable
 * 
 * @author Antonio
 * 
 */
public enum Availability {

	/**
	 * The singleton instance for an unavailable appointment.
	 * This has the numeric value of {@code 0}.
	 */
	UNAVAILABLE,
	/**
	 * The singleton instance for an available appointment.
	 * This has the numeric value of {@code 1}.
	 */
	AVAILABLE,
	/**
	 * The singleton instance for an reserved appointment.
	 * This has the numeric value of {@code 2}.
	 */
	RESERVED,
	/**
	 * The singleton instance for an arranged appointment.
	 * This has the numeric value of {@code 3}.
	 */
	ARRANGED;
	/**
     * Private cache of all the constants.
     */
    private static final Availability[] ENUMS = Availability.values();
	
	
    /**
     * Obtains an instance of {@code Availability} from an {@code int} value.
     * <p>
     * {@code Availability} is an enum representing 
     * {@code UNAVAILABLE, AVAILABLE, RESERVED, ARRANGED}.
     * This factory allows the enum to be obtained from the {@code int} value.
     * The {@code int} value follows the javadoc above.
     *
     * @param availability  the availability to represent, from 0 (Unavailable) to 3 (Arranged)
     * @return the availability singleton, not null
     * @throws AvailabilityException if the availability is invalid
     */
    public static Availability of(int availability) {
        if (availability < 0 || availability > 4) {
            throw new AvailabilityException("Invalid value for Availability: " + availability);
        }
        return ENUMS[availability];
    }
    
    /**
     * Gets the availability {@code int} value.
     * <p>
     * The values are numbered as follow: 0 (Unavailable), 1 (Available), 2 (Reserved), 3 (Arranged).
     *
     * @return the availability, from 0 (Unavailable) to 3 (Arranged)
     */
    public int getValue() {
        return ordinal();
    }
    
    /**
     * Changes an boolean input to an enumeration.<br>
     * {@code true} is changed to {@code AVAILABLE}<br>
     * {@code false} is changed to {@code UNAVAILABLE}
     * 
     * @param availability a boolean value to be changed to an enumeration
     * @return {@code AVAILABLE} if input is true, else {@code UNAVAILABLE}
     */
    public Availability changeBooleanToAvailability (Boolean availability) {
    	if (availability) {
    		return AVAILABLE;
    	} else{
    		return UNAVAILABLE;
    	}
    }
}
