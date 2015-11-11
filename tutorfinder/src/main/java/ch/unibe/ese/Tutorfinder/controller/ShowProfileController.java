package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the show profile process
 * 
 * @version 1.0
 *
 */
@Controller
public class ShowProfileController {

	@Autowired
	AppointmentService appointmentService;
	@Autowired
	UserService userService;
	@Autowired
	ProfileService profileService;
	@Autowired
	TimetableService timetableService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	PrepareFormService prepareService;

	/**
	 * Constructor for testing purposes
	 * 
	 * @param appointmentService
	 * @param userService
	 * @param profileService
	 * @param timetableService
	 * @param subjectService
	 * @param prepareService
	 */
	@Autowired
	public ShowProfileController(AppointmentService appointmentService, UserService userService,
			ProfileService profileService, TimetableService timetableService, SubjectService subjectService,
			PrepareFormService prepareService) {
		this.appointmentService = appointmentService;
		this.userService = userService;
		this.profileService = profileService;
		this.timetableService = timetableService;
		this.subjectService = subjectService;
		this.prepareService = prepareService;
	}

	/**
	 * Maps the /showProfile page to the {@code showProfile.html}.
	 * 
	 * @param authUser
	 *            {@link Principal}
	 * @return ModelAndView for Springframework with the users profile.
	 */
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView profile(Principal authUser, @RequestParam(value = "userId") long userId) {
		ModelAndView model = new ModelAndView("showProfile");

		model = prepareService.prepareModelByUserId(authUser, userId, model);
		model.addObject("makeAppointmentsForm", new MakeAppointmentsForm());

		return model;
	}

	/**
	 * Maps an request for reserve an appointment by this {@code userId} Tutor.
	 * 
	 * @param userId
	 *            identification of the {@link User}'s {@link Profile}
	 * @param appForm
	 *            holds the {@link Timestamp} and other information for the
	 *            requesting appointment
	 * @param req
	 * @param authUser
	 *            {@link Principal} logged in user, which sends the request
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/updateForm", params = "request", method = RequestMethod.POST)
	public ModelAndView requestAppointment(@RequestParam(value = "userId") long userId, MakeAppointmentsForm appForm,
			final HttpServletRequest req, Principal authUser, BindingResult result) {

		final Integer slot = Integer.valueOf(req.getParameter("request"));
		if (!result.hasErrors()) {
			User student = userService.getUserByPrincipal(authUser);
			User tutor = userService.getUserById(userId);
			appointmentService.saveFrom(appForm, slot, tutor, student);
			LocalDate date = appForm.getDate();
			DayOfWeek dow = date.getDayOfWeek();
			List<Timetable> slots = timetableService.findAllByUserAndDay(tutor, dow);
			appForm.setAppointments(appointmentService.loadAppointments(slots, tutor, date));
		}

		ModelAndView model = new ModelAndView("showProfile");
		model = prepareService.prepareModelByUserId(authUser, userId, model);
		model.addObject("makeAppointmentsForm", appForm);
		return model;
	}

	/**
	 * Loads the availability of the {@code Tutors} appointments on the
	 * requested date.
	 * 
	 * @param authUser
	 *            {@link Principal} logged in user, which sends the request for
	 *            the tutors appointment availability
	 * @param userId
	 *            identification of the {@link User}'s {@link Profile}
	 * @param appForm
	 *            holds the {@link Timestamp} and other information for the
	 *            requesting appointment
	 * @param result
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/updateForm", params = "getDate", method = RequestMethod.POST)
	public ModelAndView getDate(Principal authUser, @RequestParam(value = "userId") long userId,
			@Valid MakeAppointmentsForm appForm, BindingResult result, RedirectAttributes redirectAttributes) {

		ModelAndView model = new ModelAndView("showProfile");

		if (!result.hasErrors()) {
			User user = userService.getUserById(userId);
			LocalDate date = appForm.getDate();
			DayOfWeek dow = date.getDayOfWeek();
			List<Timetable> slots = timetableService.findAllByUserAndDay(user, dow);
			appForm.setAppointments(appointmentService.loadAppointments(slots, user, date));
		} else {
			model.addObject("error_message", "Enter a valid date (yyyy-MM-dd)");
		}

		model.addObject("makeAppointmentsForm", appForm);
		model = prepareService.prepareModelByUserId(authUser, userId, model);
		return model;
	}
}
