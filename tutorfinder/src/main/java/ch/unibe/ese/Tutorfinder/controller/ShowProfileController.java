package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the show profile process
 * 
 * @author Antonio
 *
 */
@Controller
public class ShowProfileController {

	@Autowired	ProfileDao profileDao;
	@Autowired	UserDao userDao;
	
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView showProfile(Principal user) {
		ModelAndView model = new ModelAndView("html/showProfile");
		
		model.addObject("User", userDao.findByEmail(user.getName()));
		
		model.addObject("Profile", profileDao.findOne(getUsersProfile(user)));
		
		return model;
	}
	
	private long getUsersProfile(Principal user) {
		User tmpUser = userDao.findByEmail(user.getName());
		long profileId = tmpUser.getId();
		return profileId;
	}
	
}
