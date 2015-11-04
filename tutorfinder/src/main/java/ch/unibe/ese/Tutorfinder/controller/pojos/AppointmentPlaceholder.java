package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.time.DayOfWeek;

import ch.unibe.ese.Tutorfinder.util.Availability;

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
	
	@Override
	public String toString() {
		return "Day: " + dow + ", Time: " + timeslot;
	}

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

}
