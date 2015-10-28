package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ch.unibe.ese.Tutorfinder.controller.pojos.TimetableRow;

/**
 * Builds the base of a Form containing a list of {@link TimetableRow} objects.
 * 
 * @author Antonio
 *
 */
public class UpdateTimetableForm {

	@NotNull
	private long id;
	
	private List<TimetableRow> timetableRows = new ArrayList<TimetableRow>();
	
	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<TimetableRow> getTimetableRows() {
		return timetableRows;
	}

	public void setTimetableRows(List<TimetableRow> timetableRows) {
		this.timetableRows = timetableRows;
	}
}
