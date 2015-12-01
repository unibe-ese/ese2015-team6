package ch.unibe.ese.Tutorfinder.controller.pojos.Forms;

import javax.swing.SortOrder;
import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.unibe.ese.Tutorfinder.util.SortCriteria;

/**
 * Class for handling the users input in the {@code findTutor.html}.
 * 
 * @version	1.0
 *
 */
@Component
@Scope("session")
public class FindTutorFilterForm {
	
	@NotNull
	SortCriteria criteria;
	@NotNull
	SortOrder order;
	
	public FindTutorFilterForm() {
		criteria=SortCriteria.RATING;
		order=SortOrder.DESCENDING;
	}
	
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
