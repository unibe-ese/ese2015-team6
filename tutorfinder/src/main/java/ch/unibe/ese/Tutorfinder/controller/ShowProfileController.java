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
 * @author Antonio, Florian, Nicola, Lukas
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
	 * Maps the /showProfile page to the {@code showProfile.jsp}.
	 * 
	 * @param user
	 * @return ModelAndView for Springframework with the users profile.
	 */
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView profile(Principal authUser, @RequestParam(value = "userId") long userId) {
		ModelAndView model = new ModelAndView("showProfile");
		
		model = prepareService.prepareModelByUserId(authUser, userId, model);
		model.addObject("makeAppointmentsForm", new MakeAppointmentsForm());

		return model;
	}

	@RequestMapping(value = "/updateForm", params = "request", method = RequestMethod.POST)
	public ModelAndView requestAppointment(@RequestParam(value = "userId") long userId,
			MakeAppointmentsForm appForm, final HttpServletRequest req, Principal authUser, BindingResult result) {
		
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
			model = new ModelAndView("showProfile");
		}
		model.addObject("makeAppointmentsForm", appForm);
		model = prepareService.prepareModelByUserId(authUser, userId, model);
		return model;
	}

	

}
