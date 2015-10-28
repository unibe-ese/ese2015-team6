package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Is used to make up an {@link UpdateTimetableForm} and contains 
 * it's day, time and the tutors availability values
 * 
 * @author Antonio
 *
 */
public class TimetableRow {

	private DayOfWeek day;
	private LocalTime time;
	private Boolean availability;
	
	/* Constructors */
	public TimetableRow() {
		this.day = DayOfWeek.of(1);
		this.time = LocalTime.of(0,  0);
		this.availability = false;	
	}
	
	public TimetableRow(int day, int hour, int minute, Boolean availability) {
		this.day = DayOfWeek.of(day);
		this.time = LocalTime.of(hour, minute);
		this.availability = availability;
	}
	
	/* Getters and Setters */	
	public DayOfWeek getDay() {
		return day;
	}

	public void setDay(DayOfWeek day) {
		this.day = day;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}	
	
}
