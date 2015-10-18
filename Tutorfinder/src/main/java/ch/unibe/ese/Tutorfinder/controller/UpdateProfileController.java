package ch.unibe.ese.Tutorfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @author Antonio
 *
 */
@Controller
public class UpdateProfileController {
	
	@Autowired	ProfileDao profileDao;
	@Autowired	UserDao userDao;

	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(@RequestParam(value = "profileId", required=true) long profileId) {
		ModelAndView model = new ModelAndView("updateProfile");
		model.addObject("Profile", profileDao.findOne(profileId));
		long userId = profileId;
		model.addObject("User", userDao.findOne(userId));
		
		return model;
	}
}
