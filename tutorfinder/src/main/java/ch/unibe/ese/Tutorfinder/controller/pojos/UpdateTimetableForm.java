package ch.unibe.ese.Tutorfinder.controller.pojos;

import javax.validation.constraints.NotNull;

/**
 * Builds the base of a Form containing a list of {@link TimetableRow} objects.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
public class UpdateTimetableForm {

	@NotNull
	private long id;
	
	private Boolean[][] timetable;
	
	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean[][] getTimetable() {
		return timetable;
	}

	public void setTimetable(Boolean[][] timetable) {
		this.timetable = timetable;
	}
	
}
