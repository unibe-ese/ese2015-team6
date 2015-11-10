package ch.unibe.ese.Tutorfinder.util;

import ch.unibe.ese.Tutorfinder.controller.exceptions.RoleException;

/**
 * Enumeration for the role of an user
 * @author Antonio
 *
 */
public enum Role {

	/**
	 * The singleton instance for an student.
	 * This has the numeric value of {@code 0}.
	 */
	STUDENT,
	/**
	 * The singleton instance for an tutor.
	 * This has the numeric value of {@code 1}.
	 */
	TUTOR;
	/**
	 * Private cache of all the constants.
	 */
	private static final Role[] ENUMS = Role.values();
	
	/**
	 * Obtains an instance of {@code Role} from an {@code int} value.
	 * <p>
	 * {@code Role} is an enum representing {@code STUDENT, TUTOR}.
	 * This factory allows the enum to be obtained from the {@code int} value.
	 * The {@code int} value follows the javadoc above.
	 *  
	 * @param role the role to represent, from 0 (Tutor) to 3 (Arranged)
	 * @return the role singleton, not null
	 * @throws RoleException if the availability is invalid
	 */
	public static Role of(int role) {
		if (role < 0 || role > 1) {
			throw new RoleException("Invalid value for Role: " + role);
		}
		return ENUMS[role];
	}
	
	/**
	 * Gets the role {@code int} value.
	 * <p>
	 * The values are numbered as follow: 0 (Student), 1 (Tutor).
	 * 
	 * @return the role, from 0 (Student), to 1 (Tutor).
	 */
	public int getValue() {
		return ordinal();
	}
}
