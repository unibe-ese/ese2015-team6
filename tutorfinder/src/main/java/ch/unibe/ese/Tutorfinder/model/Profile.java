package ch.unibe.ese.Tutorfinder.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

/**
 * Entity for profile, holding following values:<br>
 * {@code id} is the id of the profile and is generated automatically<br>
 * {@code email} is used for referencing between user and profile (same for id)<br>
 * {@code biography} holds an String of 255 characters for the biography of the user<br>
 * {@code region} holds an String of 255 characters for the region of the user<br>
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Entity
public class Profile {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private String email;

	private String biography;
	
	private String region;
	
	@NumberFormat(style=Style.CURRENCY)
	private BigDecimal wage;
	
	
	/* Constructors */
	public Profile() {
		super();
	}
	
	public Profile(String email) {
		super();
		this.email = email;
	}

	
	/* Getters and setters */
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

	public BigDecimal getWage() {
		return wage;
	}

	public void setWage(BigDecimal wage) {
		this.wage = wage;
	}
	
}
