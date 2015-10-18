package ch.unibe.ese.Tutorfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @author Antonio
 *
 */
@Controller
public class UpdateProfileController {

	@RequestMapping(value = "/updateProfile", method = RequestMethod.GET)
	public ModelAndView updateProfile() {
		ModelAndView model = new ModelAndView();
		
		return model;
	}
}
