package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import javax.validation.constraints.NotNull;

/**
 * Class for validating the users input in the {@code showProfile.html}.
 * 
 * @version	1.0
 *
 */
public class FindTutorForm {
	
	@NotNull
	private String subject;

	
	/* Getters and Setters */
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	

}
