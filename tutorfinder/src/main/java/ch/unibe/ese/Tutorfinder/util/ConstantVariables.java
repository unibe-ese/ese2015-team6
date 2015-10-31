package ch.unibe.ese.Tutorfinder.util;

/**
 * Holds all constant variables of the project,
 * so they need only to be changed in one location.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
public class ConstantVariables {
	
	/* Password constants */
	public static final int PASSWORD_MIN_LENGHT = 8;
	
	public static final int PASSWORD_MAX_LENGHT = 25;
	
	public static final String PASSWORD_ERRORMESSAGE = "The length must be between {min} and {max}";
	
	public static final String CONFIRMPASSWORD_ERRORMESSAGE = "Password and password confirmation does not match";
	
	/* Wage constants */
	public static final int WAGE_VALUE = 0;
	
	public static final String WAGE_ERRORMESSAGE = "The wage must be positiv";
	
	
	/* Email constants */
	public static final String EMAIL_REGEX_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	public static final String EMAIL_ERRORMESSAGE = "Must be valid email address";
	
	/* Tutor or Student */
	public static final String TUTOR = "TUTOR";
	
	public static final String STUDENT = "STUDENT";
	
}