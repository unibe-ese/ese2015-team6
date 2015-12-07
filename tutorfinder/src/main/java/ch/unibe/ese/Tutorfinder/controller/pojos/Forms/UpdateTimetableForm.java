package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

/**
 * Builds the base of a Form containing a list of {@link TimetableRow} objects.
 * 
 * @version	1.0
 *
 */
public class UpdateTimetableForm {

	@NotNull
	private long id;
	
	private Boolean[][] timetable = new Boolean[ConstantVariables.TIMESLOTS][ConstantVariables.DAYS];
	
	
	/* Constructor */
	public UpdateTimetableForm() {
		for(Boolean[] row : timetable)
			Arrays.fill(row, false);
	}
	
	
	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean[][] getTimetable() {
		return timetable.clone();
	}

	public void setTimetable(Boolean[][] timetable) {
		assert (timetable.length == ConstantVariables.TIMESLOTS);
		for( int i=0; i < ConstantVariables.TIMESLOTS; i++) {
			assert (timetable[i].length == ConstantVariables.DAYS);
		}
		this.timetable = timetable.clone();
	}
	
}
