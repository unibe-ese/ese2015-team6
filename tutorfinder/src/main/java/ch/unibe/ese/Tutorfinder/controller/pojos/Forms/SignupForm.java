
package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.ScriptAssert;

import ch.unibe.ese.Tutorfinder.util.ConstantVariables;


/**
 * Class for validating the users input in the {@code register.html}.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@ScriptAssert(lang="javascript", script = "_this.password.equals(_this.confirmPassword)",
message = ConstantVariables.CONFIRMPASSWORD_ERRORMESSAGE)
public class SignupForm {
	
	@NotNull
	private long id;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@NotNull
	@Pattern(regexp = ConstantVariables.EMAIL_REGEX_PATTERN, 
    		message = ConstantVariables.EMAIL_ERRORMESSAGE)
	private String email;
	
	@NotNull
	@Size(min = ConstantVariables.PASSWORD_MIN_LENGTH, 
			max = ConstantVariables.PASSWORD_MAX_LENGTH, 
			message = ConstantVariables.PASSWORD_ERRORMESSAGE)
	private String password;
	
	private String confirmPassword;
	
	@NotNull
	private boolean tutor;
	
	
	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isTutor() {
		return tutor;
	}

	public void setTutor(boolean tutor) {
		this.tutor = tutor;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}