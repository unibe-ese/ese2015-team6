package ch.unibe.ese.Tutorfinder.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

/**
 * Entity for profile, holding following values:<br>
 * {@code id} is the id of the profile and is generated automatically<br>
 * {@code email} is used for referencing between user and profile (same for id)<br>
 * {@code biography} holds an String of 255 characters for the biography of the user<br>
 * {@code region} holds an String of 255 characters for the region of the user<br>
 * {@code wage} holds the actual wage from the tutor<br>
 * 
 * @version	1.0
 *
 */
@Entity
public class Profile {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private String email;

	@Lob
	private String biography;
	
	private String region;
	
	private String university;
	
	private String language;
	
	@NumberFormat(style=Style.CURRENCY)
	private BigDecimal wage;
	
	private BigDecimal rating;
	
	private long countedRatings;
	
	
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
		assert email != null : "Profiles email is not allowed to be null!";
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

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public long getCountedRatings() {
		return countedRatings;
	}

	public void setCountedRatings(long countedRatings) {
		this.countedRatings = countedRatings;
	}
	
	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", email=" + email + ", biography=" + biography + ", region=" + region + ", wage="
				+ wage + "]";
	}

}
