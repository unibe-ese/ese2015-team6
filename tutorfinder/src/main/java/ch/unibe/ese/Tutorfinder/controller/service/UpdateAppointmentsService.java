package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidAppointmentException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateAppointmentsForm;

public interface UpdateAppointmentsService {

	public UpdateAppointmentsForm saveFrom(UpdateAppointmentsForm updateAppointmentsForm, Principal user) throws InvalidAppointmentException;
}
