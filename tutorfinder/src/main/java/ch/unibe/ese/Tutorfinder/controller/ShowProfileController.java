package ch.unibe.ese.Tutorfinder.controller;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
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
	@Autowired
	AppointmentDao appointmentDao;

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
			User user = resolveUserId(userId);
			LocalDate date = appForm.getDate();
			DayOfWeek dow = date.getDayOfWeek();
			List<Timetable> slots = timetableDao.findAllByUserAndDay(user, dow);
			List<AppointmentPlaceholder> tmpList = new ArrayList<AppointmentPlaceholder>();
			
			for (Timetable slot : slots) {
				int hours = slot.getTime();
				
				LocalDateTime dateTime = LocalDateTime.from(date.atStartOfDay());
				dateTime.plusHours(hours);
				Timestamp timestamp = Timestamp.valueOf(dateTime);
				
				Appointment tmpAppointment = appointmentDao.findByTutorAndTimestamp(user, timestamp);
				AppointmentPlaceholder placeholder;
				
				if (tmpAppointment != null) {
					placeholder = new AppointmentPlaceholder();
					placeholder.setAvailability(tmpAppointment.getAvailability());
					placeholder.setDow(dow);
					placeholder.setTimeslot(hours);
				} else {
					placeholder = new AppointmentPlaceholder(dow, hours);
				}
				tmpList.add(placeholder);
			}
			
			appForm.setAppointments(tmpList);
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
