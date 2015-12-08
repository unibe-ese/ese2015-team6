package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import javax.validation.constraints.NotNull;

/**
 * Form to get a password confirmation for the changing from
 * {@code student} to {@code tutor}.
 * 
 *@version 2.0
 */
public class PasswordConfirmationForm {

	@NotNull
	private String password;

	/* Getters and Setters */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
