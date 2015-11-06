package ch.unibe.ese.Tutorfinder.util;

/**
 * Holds all constant variables of the project,
 * so they need only to be changed in one location.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
// TODO load from configuration file (for an admin to change)
public class ConstantVariables {
	
	/* Password constants */
	public static final int PASSWORD_MIN_LENGTH = 8;
	
	public static final int PASSWORD_MAX_LENGTH = 25;
	
	public static final String PASSWORD_ERRORMESSAGE = "Enter a password with a length between {min} and {max} characters";
	
	public static final String CONFIRMPASSWORD_ERRORMESSAGE = "Password and password confirmation do not match";
	
	/* Wage constants */
	public static final int WAGE_VALUE = 0;
	
	public static final String WAGE_ERRORMESSAGE = "The wage must be positive";
	
	
	/* Email constants */
	public static final String EMAIL_REGEX_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	public static final String EMAIL_ERRORMESSAGE = "Enter a valid email address";
	
	/* Tutor or Student */
	public static final String TUTOR = "TUTOR";
	
	public static final String STUDENT = "STUDENT";

	/* Time and Day */
	public static final int TIMESLOTS = 24;

	public static final int DAYS = 7;
	
}

