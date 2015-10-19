package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateProfileService;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @author Antonio
 *
 */
@Controller
public class UpdateProfileController {
	
	@Autowired	UpdateProfileService updateProfileService;
	@Autowired	ProfileDao profileDao;
	@Autowired	UserDao userDao;

	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");
		model.addObject("User", userDao.findByEmail(user.getName()));
		User tmpUser = userDao.findByEmail(user.getName());
		long profileId = tmpUser.getId();
		model.addObject("Profile", profileDao.findOne(profileId));
		
		return model;
	}
	
	@RequestMapping(value ="/updateProfile", method = RequestMethod.POST)
	public ModelAndView editBiography(@Valid UpdateProfileForm updateProfileForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model;
		if (!result.hasErrors()) {
            try {
            	updateProfileService.saveFrom(updateProfileForm);
            	model = new ModelAndView("home");
            } catch (InvalidUserException e) {
            	model = new ModelAndView("updateProfile");
            	model.addObject("page_error", e.getMessage());
            }
        } else {
        	model = new ModelAndView("updateProfile");
        }
		return model;
	}
}
