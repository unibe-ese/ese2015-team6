package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the appointments process.
 * 
 */
@Controller
public class AppointmentsOverviewController {
	
	@Autowired
	AppointmentService appointmentService;
	@Autowired
	UserService userService;

	/**
	 * Maps the /appointments page to the {@code appointmentsOverview.html}.
	 * 
	 * @param authUser
	 *            actually logged in user, is used to get the users appointments
	 *            information and shows it to the user to allow handling them.
	 * @return ModelAndView for Spring framework with the users AppoitnemtnOverview.
	 */
	@RequestMapping(value = "/appointments", method = RequestMethod.GET)
	public ModelAndView appointments(Principal authUser) {
		ModelAndView model = new ModelAndView("appointmentsOverview");
		
		ModelAndView resultModel = prepareAppointmentsOverview(model, authUser);

		return resultModel;
	}
	
	@RequestMapping(value="/editAppointments", params = "decline", method = RequestMethod.POST)
	public ModelAndView decline(final HttpServletRequest req, Principal authUser) {
		ModelAndView model = new ModelAndView("appointmentsOverview");
		
		final long appointmentId = Long.valueOf(req.getParameter("decline"));
		appointmentService.updateAppointment(Availability.AVAILABLE, appointmentId);
		
		ModelAndView resultModel = prepareAppointmentsOverview(model, authUser);
		
		
		return resultModel;
	}
	
	@RequestMapping(value="/editAppointments", params = "confirm", method = RequestMethod.POST)
	public ModelAndView confirm(final HttpServletRequest req, Principal authUser) {
		ModelAndView model = new ModelAndView("appointmentsOverview");
		
		final long appointmentId = Long.valueOf(req.getParameter("confirm"));
		appointmentService.updateAppointment(Availability.ARRANGED, appointmentId);
		
		ModelAndView resultModel = prepareAppointmentsOverview(model, authUser);
		
		
		return resultModel;
	}
	
	/**
	 * Prepares the model for the {@code appointmentsOverview.html} site, which means 
	 * it adds the confirmed, reserved and past appointments of the tutor in the model.
	 * 
	 * @param model a new {@code appointmentsOverview.html}
	 * @param authUser {@link Principal} actual logged in user
	 * @return model with the new objects
	 */
	public ModelAndView prepareAppointmentsOverview(ModelAndView model, Principal authUser) {
		User tmpUser = userService.getUserByPrincipal(authUser);
		model.addObject("authUser", tmpUser);
		
		model.addObject("Arranged", appointmentService.getFutureAppointments(tmpUser, Availability.ARRANGED));
		model.addObject("Reserved", appointmentService.getFutureAppointments(tmpUser, Availability.RESERVED));
		model.addObject("Past", appointmentService.getPastAppointments(tmpUser, Availability.ARRANGED));

		return model;
	}
	
}
