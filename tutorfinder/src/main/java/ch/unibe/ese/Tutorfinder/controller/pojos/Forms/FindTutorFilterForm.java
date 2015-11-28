package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import javax.swing.SortOrder;
import javax.validation.constraints.NotNull;

import ch.unibe.ese.Tutorfinder.util.SortCriteria;

/**
 * Class for handling the users input in the {@code findTutor.html}.
 * 
 * @version	1.0
 *
 */
public class FindTutorFilterForm {
	
	@NotNull
	SortCriteria criteria;
	@NotNull
	SortOrder order;
	
	public SortCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(SortCriteria criteria) {
		this.criteria = criteria;
	}

	public SortOrder getOrder() {
		return order;
	}

	public void setOrder(SortOrder order) {
		this.order = order;
	}
	
}
