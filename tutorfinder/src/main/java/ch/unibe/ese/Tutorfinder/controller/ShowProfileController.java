package ch.unibe.ese.Tutorfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
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

	@Autowired	ProfileDao profileDao;
	@Autowired	UserDao userDao;
	@Autowired	SubjectDao subjectDao;
	
	
	/**
	 * Maps the /showProfile page to the {@code showProfile.jsp}.
	 * 
	 * @param user
	 * @return ModelAndView for Springframework with the users profile.
	 */
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView profile(@RequestParam(value="userId",required= true)int userId) { 
		ModelAndView model = new ModelAndView("showProfile");
		
		User tmpUser = userDao.findById(userId);
		model.addObject("User", tmpUser);
		model.addObject("Subjects", subjectDao.findAllByUser(tmpUser));
		
		model.addObject("Profile", profileDao.findByEmail(tmpUser.getEmail()));
		
		return model;
	}
	
}
