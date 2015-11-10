package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
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

	/**
	 * Maps the /showProfile page to the {@code showProfile.jsp}.
	 * 
	 * @param user
	 * @return ModelAndView for Springframework with the users profile.
	 */
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView profile(Principal authUser, @RequestParam(value = "userId") long userId) {
		ModelAndView model = new ModelAndView("showProfile");
		
		model = prepareModelByUserId(authUser, userId, model);
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
			appForm.setAppointments(loadAppointments(slots, tutor, date));
		}
		
		ModelAndView model = new ModelAndView("showProfile");
		model = prepareModelByUserId(authUser, userId, model);		
		model.addObject("makeAppointmentsForm", appForm);
		return model;
	}

	@RequestMapping(value = "/updateForm", params = "getDate", method = RequestMethod.POST)
	public ModelAndView getDate(Principal authUser, @RequestParam(value = "userId") long userId,
			MakeAppointmentsForm appForm, BindingResult result) {
		if (!result.hasErrors()) {
			User user = userService.getUserById(userId);
			LocalDate date = appForm.getDate();
			DayOfWeek dow = date.getDayOfWeek();
			List<Timetable> slots = timetableService.findAllByUserAndDay(user, dow);
			appForm.setAppointments(loadAppointments(slots, user, date));
		}
		ModelAndView model = new ModelAndView("showProfile");
		model.addObject("makeAppointmentsForm", appForm);
		model = prepareModelByUserId(authUser, userId, model);
		return model;
	}

	//TODO refactor in service
	private List<AppointmentPlaceholder> loadAppointments(List<Timetable> slots, User user, LocalDate date) {
		
		List<AppointmentPlaceholder> tmpList = appointmentService.findByTutorAndDate(user, date);
		
		for (Timetable slot : slots) {
			int hours = slot.getTime();

			LocalDateTime dateTime = LocalDateTime.from(date.atStartOfDay());
			dateTime = dateTime.plusHours(hours);
			Timestamp timestamp = Timestamp.valueOf(dateTime);

			if (appointmentService.findByTutorAndTimestamp(user, timestamp) == null) {
				AppointmentPlaceholder placeholder = new AppointmentPlaceholder(date.getDayOfWeek(), hours);
				tmpList.add(placeholder);
			}
		}
		return tmpList;
	}

	//TODO refactor in service
	private ModelAndView prepareModelByUserId(Principal authUser, long userId, ModelAndView model) {
		User tmpAuthUser = userService.getUserByPrincipal(authUser);
		model.addObject("authUser", tmpAuthUser);
		
		User tmpUser = userService.getUserById(userId);
		model.addObject("DisplayedUser", tmpUser);
		model.addObject("Subjects", subjectService.getAllSubjectsByUser(tmpUser));
		model.addObject("Profile", profileService.getProfileByEmail(tmpUser.getEmail()));
		return model;
	}

}
