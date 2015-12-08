package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**
 * Class for validating the users input in the 
 * {@code appointmentOverview.html} rating field.
 * 
 * @version	2.0
 *
 */
public class RateTutorForm {

	@NotNull
	private BigDecimal rating;

	/* Getters and Setters */
	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	
	
}
