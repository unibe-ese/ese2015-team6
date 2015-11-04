package ch.unibe.ese.Tutorfinder.controller;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

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
	ProfileDao profileDao;
	@Autowired
	UserDao userDao;
	@Autowired
	SubjectDao subjectDao;
	@Autowired
	TimetableDao timetableDao;

	/**
	 * Maps the /showProfile page to the {@code showProfile.jsp}.
	 * 
	 * @param user
	 * @return ModelAndView for Springframework with the users profile.
	 */
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView profile(@RequestParam(value = "userId", required = true) int userId) {
		ModelAndView model = new ModelAndView("showProfile");

		model = prepareModelByUserId(userId, model);
		model.addObject("makeAppointmentsForm", new MakeAppointmentsForm());

		return model;
	}

	@RequestMapping(value = "/updateForm", params = "request", method = RequestMethod.POST)
	public ModelAndView requestAppointment() {
		ModelAndView model = null; // TODO
		return model;
	}

	@RequestMapping(value = "/updateForm", params = "getDate", method = RequestMethod.POST)
	public ModelAndView getDate(@RequestParam(value = "userId", required = true) int userId,
			MakeAppointmentsForm appForm, BindingResult result) {
		ModelAndView model = new ModelAndView("showProfile");
		if (!result.hasErrors()) {
			DayOfWeek dow = appForm.getDate().getDayOfWeek();
			List<Timetable> slots = timetableDao.findAllByUserAndDay(resolveUserId(userId), dow);
			appForm.setAppointmentList(null); //TODO generate list
		}
		model.addObject("makeAppointmentsForm", appForm);
		model = prepareModelByUserId(userId, model);
		return model;
	}	

	private ModelAndView prepareModelByUserId(int userId, ModelAndView model) {
		User tmpUser = resolveUserId(userId);
		model.addObject("User", tmpUser);
		model.addObject("Subjects", subjectDao.findAllByUser(tmpUser));
		model.addObject("Profile", profileDao.findByEmail(tmpUser.getEmail()));
		return model;
	}

	private User resolveUserId(int userId) {
		return userDao.findById(userId);
	}

}
