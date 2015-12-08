package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.RateTutorForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the appointments process.
 * 
 * @version 2.0
 * 
 */
@Controller
public class AppointmentsOverviewController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private UserService userService;

	/**
	 * Constructor for testing purposes
	 * 
	 * @param appointmentService
	 * @param userService
	 */
	@Autowired
	public AppointmentsOverviewController(AppointmentService appointmentService, UserService userService) {
		this.appointmentService = appointmentService;
		this.userService = userService;
	}

	/**
	 * Maps the /appointments page to the {@code appointmentsOverview.html}.
	 * 
	 * @param authUser
	 *            actually logged in user, is used to get the users appointments
	 *            information and shows it to the user to allow handling them.
	 * @return ModelAndView for Spring framework with the users
	 *         AppoitnemtnOverview.
	 */
	@RequestMapping(value = "/appointments", method = RequestMethod.GET)
	public ModelAndView appointments(Principal authUser) {
		ModelAndView model = new ModelAndView("appointmentsOverview");

		model = prepareAppointmentsOverview(model, authUser);

		return model;
	}

	/**
	 * Maps the /editAppointments page with the parameter decline, which means
	 * that it sets the availability of the requested {@link Appointment} to
	 * {@code AVAILABLE}
	 * 
	 * @param req
	 * @param authUser
	 *            actually logged in user, is used to get the users appointments
	 *            information and shows it to the user to allow handling them.
	 * @return ModelAndView for Spring framework with the users updated
	 *         AppoitnemtnOverview.
	 */
	@RequestMapping(value = "/editAppointments", params = "decline", method = RequestMethod.POST)
	public ModelAndView decline(final HttpServletRequest req, Principal authUser) {
		ModelAndView model = new ModelAndView("appointmentsOverview");

		final Long appointmentId = Long.valueOf(req.getParameter("decline"));
		appointmentService.updateAppointment(Availability.AVAILABLE, appointmentId);

		model = prepareAppointmentsOverview(model, authUser);

		return model;
	}

	/**
	 * Maps the /editAppointments page with the parameter confirm, which means
	 * that it sets the availability of the requested {@link Appointment} to
	 * {@code ARRANGED}
	 * 
	 * @param req
	 * @param authUser
	 *            actually logged in user, is used to get the users appointments
	 *            information and shows it to the user to allow handling them.
	 * @return ModelAndView for Spring framework with the users updated
	 *         AppoitnemtnOverview.
	 */
	@RequestMapping(value = "/editAppointments", params = "confirm", method = RequestMethod.POST)
	public ModelAndView confirm(final HttpServletRequest req, Principal authUser) {
		ModelAndView model = new ModelAndView("appointmentsOverview");

		final Long appointmentId = Long.valueOf(req.getParameter("confirm"));
		appointmentService.updateAppointment(Availability.ARRANGED, appointmentId);

		model = prepareAppointmentsOverview(model, authUser);

		return model;
	}

	/**
	 * Maps the /rateAppointment page with the parameter rate, which means that
	 * it saves the rate from the {@link RateTutorForm} to the appointment, and
	 * calls the method {@code updateRating(...)} in the {@link ProfileService}.
	 * This causes the Tutors rate to be recalculate.
	 * 
	 * @param req
	 *            parameter to know to which appointment the rate belongs
	 * @param authUser
	 *            actual logged in user {@link Principal}
	 * @param form
	 *            which holds the users input value for the rating
	 * @param result
	 * @return ModelAndView for Spring framework with the users updated
	 *         AppoitnemtnOverview.
	 */
	@RequestMapping(value = "/rateAppointment", params = "rate", method = RequestMethod.POST)
	public ModelAndView rate(final HttpServletRequest req, Principal authUser, @Valid RateTutorForm form,
			BindingResult result) {
		ModelAndView model = new ModelAndView("appointmentsOverview");

		final Long appointmentId = Long.valueOf(req.getParameter("rate"));
		appointmentService.rateTutorForAppointment(appointmentId, form.getRating());

		model = prepareAppointmentsOverview(model, authUser);

		return model;
	}

	/**
	 * Prepares the model for the {@code appointmentsOverview.html} site, which
	 * means it adds the confirmed, reserved and past appointments of the tutor
	 * in the model.
	 * 
	 * @param model
	 *            a new {@code appointmentsOverview.html}
	 * @param authUser
	 *            {@link Principal} actual logged in user
	 * @return model with the new objects
	 */
	private ModelAndView prepareAppointmentsOverview(ModelAndView model, Principal authUser) {
		User tmpUser = userService.getUserByPrincipal(authUser);
		model.addObject("authUser", tmpUser);

		model.addObject("arranged", appointmentService.getFutureAppointments(tmpUser, Availability.ARRANGED));
		model.addObject("reserved", appointmentService.getFutureAppointments(tmpUser, Availability.RESERVED));
		model.addObject("pending", appointmentService.getPendingAppointments(tmpUser));
		model.addObject("past", appointmentService.getPastAppointments(tmpUser, Availability.ARRANGED));
		model.addObject("visited", appointmentService.getPastAppointmentsAsStudent(tmpUser));
		model.addObject("confirmed", appointmentService.getFutureAppointmentsAsStudent(tmpUser));

		model.addObject("rateTutorForm", new RateTutorForm());

		return model;
	}

}
