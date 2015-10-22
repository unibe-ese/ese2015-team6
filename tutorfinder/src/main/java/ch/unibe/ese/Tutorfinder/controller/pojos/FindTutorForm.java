package ch.unibe.ese.Tutorfinder.controller.pojos;

import javax.validation.constraints.NotNull;

public class FindTutorForm {
	
	@NotNull
	private String subject;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	

}
