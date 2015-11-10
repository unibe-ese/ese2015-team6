package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update Timetable process
 * 
 * @version 1.0
 *
 */
@Controller
public class TimetableController {
	
	@Autowired
	TimetableService timetableService;
	@Autowired
	PrepareFormService prepareFormService;
	
	/**
	 * Maps the /updateTimetable action to the {@code updateProfile.html}.
	 * Furthermore it saves the new {@link UpdateTimetableForm} as basic 
	 * {@link Timetable} of the {@link Principal} user.
	 * 
	 * @param updateTimetableForm holds the new available {@code Timeslot}'s of the user
	 * @param result
	 * @param authUser {@link Principal} user which wants to change his basic {@link Timetable}
	 * @return {@link ModelAndView} with the {@code updateProfile.html} mapping
	 */
	@RequestMapping(value = "/updateTimetable", method = RequestMethod.POST)
	public ModelAndView updateTimetable(@Valid UpdateTimetableForm updateTimetableForm, BindingResult result,
			Principal authUser) {
		ModelAndView model = new ModelAndView("updateProfile");
		if (!result.hasErrors()) {
			try {
				timetableService.saveFrom(updateTimetableForm, authUser);
			} catch (InvalidTimetableException e) {
				// TODO Handling
				System.err.println("Timetable error");
			}
		}

		model = prepareFormService.prepareForm(authUser, model);
		model.addObject("updateTimetableForm", updateTimetableForm);
		return model;
	}

}
