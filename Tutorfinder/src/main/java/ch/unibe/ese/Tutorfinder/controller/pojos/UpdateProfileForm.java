package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;

public class UpdateProfileForm {

	@NotNull
	private long id;
	
	@Email
	@NotNull
	private String email;

	private String biography;
	private String region;
	private String imgPath;
	
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
	
	public String getImgPath() {
		return imgPath;
	}
	
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public BigDecimal getWage() {
		return wage;
	}
	
	public void setWage(BigDecimal wage) {
		this.wage = wage;
	}

}