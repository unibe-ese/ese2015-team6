package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

/**
 * Class for validating the users input in the {@code message.html}.
 * 
 */
public class MessageForm {

	@NotNull
	private Long receiverId;

	@NotBlank(message = ConstantVariables.SUBJECT_ERRORMESSAGE)
	private String subject;

	@NotBlank(message = ConstantVariables.MESSAGE_ERRORMESSAGE)
	private String message;

	
	/* Getters and Setters */
	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
