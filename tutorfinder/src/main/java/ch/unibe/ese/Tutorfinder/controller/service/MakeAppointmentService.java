package ch.unibe.ese.Tutorfinder.controller.service;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.User;

public interface MakeAppointmentService {

	void saveFrom(MakeAppointmentsForm appForm, Integer slot, User tutor, User student);

}
