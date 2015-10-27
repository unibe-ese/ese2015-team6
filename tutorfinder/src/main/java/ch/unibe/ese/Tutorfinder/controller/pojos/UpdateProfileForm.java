package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.ScriptAssert;

/**
 * Class for validating the users input in the {@code updateProfile.jsp}.
 * 
 * @author Antonio
 *
 */
@ScriptAssert(lang="javascript", script = "_this.password.equals(_this.confirmPassword)",
		message="Password and password confirmation does not match")
public class UpdateProfileForm {

	@NotNull
	private long id;
	
	@Email
	@NotNull
	private String email;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@Size(min=8, max=25, message="The length must be between {min} and {max}")
	private String password;
	
	private String confirmPassword;
	
	private String biography;
	
	private String region;
	
	private BigDecimal wage;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
