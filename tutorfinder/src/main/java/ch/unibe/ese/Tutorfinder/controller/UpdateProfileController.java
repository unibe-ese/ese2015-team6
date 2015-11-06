package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidProfileException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateSubjectsService;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateTimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Controller
public class UpdateProfileController {

	@Autowired
	UpdateSubjectsService updateSubjectsService;
	@Autowired
	UpdateTimetableService updateTimetableService;
	@Autowired
	UserService userService;
	@Autowired
	TimetableService timetableService;
	@Autowired
	ProfileService profileService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	PrepareFormService prepareFormService;

	/**
	 * Maps the /editProfile page to the {@code updateProfile.jsp}.
	 * 
	 * @param user
	 *            actually logged in user, is used to get the users profile
	 *            information and shows it to the user to allow editing the
	 *            information.
	 * @return ModelAndView for Spring framework with the users editable
	 *         profile.
	 */
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");

		model = prepareFormService.prepareForm(user, model);
		return model;
	}

	/**
	 * Handles users form input with let it validate by the
	 * {@code UpdarProfileForm} class and passing it to the
	 * {@code UpdateProfileServiceImpl} class which saves the new information to
	 * the database.
	 * 
	 * @param user
	 *            actually logged in user, is used to get the users profile
	 *            information and shows it to the user to allow editing the
	 *            information.
	 * @param updateProfileForm
	 *            class to validate the users form input.
	 * @param result
	 * @param redirectAttributes
	 * @return ModelAndView for Spring framework with the users new and editable
	 *         profile.
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Principal user, @Valid UpdateProfileForm updateProfileForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model;
		if (!result.hasErrors()) {
			try {
				profileService.saveFrom(updateProfileForm, userService.getUserByPrincipal(user));
				model = new ModelAndView("updateProfile");
				// TODO show success message to the user
			} catch (InvalidProfileException e) {
				model = new ModelAndView("updateProfile");
				model.addObject("page_error", e.getMessage());
				// TODO show error massage to the user
			}
		} else {
			model = new ModelAndView("updateProfile");
			model.addObject("User", userService.getUserByPrincipal(user));
			model.addObject("updateSubjectsForm",
					prepareFormService.getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(userService.getUserByPrincipal(user))));
			return model;
		}
		model = prepareFormService.prepareForm(user, model);
		model.addObject("updateProfileForm", updateProfileForm);

		return model;
	}

	/**
	 * Converts empty fields to null values
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
