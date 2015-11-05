package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ch.unibe.ese.Tutorfinder.controller.pojos.Row;

/**
 * Builds the base of a Form containing a lit of {@link Row} objects.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
public class UpdateSubjectsForm {
	
	@NotNull
	private long id;
	
	private List<Row> rows = new ArrayList<Row>();
	
	
	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	
}
