package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.time.DayOfWeek;

import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

/**
 * Object to save temporarily the basic {@link Timetable} of 
 * an {@link User} and the reserved or arranged {@link Appointments}.
 * 
 * @version	1.0
 *
 */
public class AppointmentPlaceholder {
	private Availability availability;
	private DayOfWeek dow;
	private int timeslot;
	
	public AppointmentPlaceholder() {
		availability = Availability.UNAVAILABLE;
	}
	
	public AppointmentPlaceholder(DayOfWeek dow, int timeslot) {
		this.dow = dow;
		this.timeslot = timeslot;
		availability = Availability.AVAILABLE;
	}

	
	/* Getters and Setters */
	public Availability getAvailability() {
		return availability;
	}
	
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
	
	public DayOfWeek getDow() {
		return dow;
	}
	
	public void setDow(DayOfWeek dow) {
		this.dow = dow;
	}
	
	public int getTimeslot() {
		return timeslot;
	}
	
	public void setTimeslot(int timeslot) {
		this.timeslot = timeslot;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availability == null) ? 0 : availability.hashCode());
		result = prime * result + ((dow == null) ? 0 : dow.hashCode());
		result = prime * result + timeslot;
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
		AppointmentPlaceholder other = (AppointmentPlaceholder) obj;
		if (availability != other.availability)
			return false;
		if (dow != other.dow)
			return false;
		if (timeslot != other.timeslot)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Availability: " + availability + ", Day: " + dow + ", Time: " + timeslot;
	}

}
