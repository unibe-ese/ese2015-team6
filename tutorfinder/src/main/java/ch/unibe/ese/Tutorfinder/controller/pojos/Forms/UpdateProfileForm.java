package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.ScriptAssert;

import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

/**
 * Class for validating the users input in the {@code updateProfile.html}.
 * 
 * @version	2.0
 *
 */
@ScriptAssert(lang = "javascript", 
script = "if(_this.password !== null) {_this.password.equals(_this.confirmPassword)} else {"
		+ "if(_this.confirmPassword !== null) {_this.confirmPassword.equals(_this.password)} else {true}}", 
message = ConstantVariables.CONFIRMPASSWORD_ERRORMESSAGE)
public class UpdateProfileForm {

	@NotNull
	private long id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@Size(min = ConstantVariables.PASSWORD_MIN_LENGTH, 
			max = ConstantVariables.PASSWORD_MAX_LENGTH, 
			message = ConstantVariables.PASSWORD_ERRORMESSAGE)
	private String password;

	private String confirmPassword;

	private String biography;

	private String region;
	
	@Min(value = ConstantVariables.WAGE_VALUE, 
			message = ConstantVariables.WAGE_ERRORMESSAGE)
	private BigDecimal wage;
	
	private String university;
	
	private String language;

	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public BigDecimal getWage() {
		return wage;
	}

	public void setWage(BigDecimal wage) {
		this.wage = wage;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

}
