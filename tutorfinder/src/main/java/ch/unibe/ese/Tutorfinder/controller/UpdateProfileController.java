package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.PasswordConfirmationForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @version 1.0
 *
 */
@Controller
public class UpdateProfileController {

	@Autowired
	private UserService userService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private PrepareFormService prepareFormService;
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * Constructor for testing purposes
	 * 
	 * @param userService
	 * @param profileService
	 * @param prepareFormService
	 * @param authenticationManager
	 */
	@Autowired
	public UpdateProfileController(UserService userService, ProfileService profileService, 
			PrepareFormService prepareFormService, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.profileService = profileService;
		this.prepareFormService = prepareFormService;
		this.authenticationManager = authenticationManager;
	}
	
	@ModelAttribute("updateProfileForm")
	public UpdateProfileForm getUpdateProfileForm(Principal authUser) {
		return prepareFormService.getFormWithValues(authUser);
	}
	
	/**
	 * Maps the /editProfile page to the {@code updateProfile.html}.
	 * 
	 * @param user
	 *            actually logged in user, is used to get the users profile
	 *            information and shows it to the user to allow editing the
	 *            information.
	 * @return ModelAndView for Spring framework with the users editable
	 *         profile.
	 */
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(Principal authUser, RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView("updateProfile");
		model = prepareFormService.prepareForm(authUser, model);
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
	public ModelAndView update(Principal authUser, @Valid @ModelAttribute("updateProfileForm") UpdateProfileForm updateProfileForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView("redirect:/editProfile");

		if (!result.hasErrors()) {
			profileService.saveFrom(updateProfileForm, userService.getUserByPrincipal(authUser));
			redirectAttributes.addFlashAttribute("update_msg","Your profile information has been updated");
		}
		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateProfileForm", result);
		redirectAttributes.addFlashAttribute("updateProfileForm", updateProfileForm);
		return model;
	}

	/**
	 * Handles users form input with let it validate by the
	 * {@code PasswordConfirmationForm} class and allows the changing of the
	 * users {@code Role} from {@code STUDENT} to {@code TUTOR}
	 * 
	 * @param authUser
	 *            {@link Principal} which is actual logged in and wants to
	 *            become a tutor
	 * @param passwordConfirmationForm
	 *            saves temporarily the users confirmation password
	 * @param result
	 * @param redirectAttributes
	 * @return {@link ModelAndView} with the {@code updateProfile.html} view for
	 *         an Tutor if the password confirmation war correct els the same
	 *         view for an Student
	 */
	@RequestMapping(value = "/changeRole", method = RequestMethod.POST)
	public ModelAndView becomeTutor(Principal authUser, @Valid PasswordConfirmationForm passwordConfirmationForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		User user = userService.getUserByPrincipal(authUser);
		ModelAndView model = new ModelAndView("updateProfile");

		if (passwordConfirmationForm.getPassword() != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(passwordConfirmationForm.getPassword(), user.getPassword())) {
				userService.changeToTutor(user);
				Authentication authenticate = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),
								passwordConfirmationForm.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authenticate);
				model = prepareFormService.prepareForm(authenticate, model);
			} else {
				model = prepareFormService.prepareForm(authUser, model);
			}
		}

		return model;
	}

	/**
	 * Converts empty fields to null values
	 * 
	 * @param binder
	 */
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
