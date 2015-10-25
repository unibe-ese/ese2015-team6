package ch.unibe.ese.Tutorfinder.controller.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class UpdateSubjectsForm {
	
	@NotNull
	private long id;
	
	private List<Row> rows = new ArrayList<Row>();
	
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
