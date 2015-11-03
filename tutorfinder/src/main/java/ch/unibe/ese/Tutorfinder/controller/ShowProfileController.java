package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
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
	
	
	/**
	 * Maps the /showProfile page to the {@code showProfile.jsp}.
	 * 
	 * @param user
	 * @return ModelAndView for Springframework with the users profile.
	 */
	//FIXME don't use Principal user, it allows only access to the actually logged in profile!!
	
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView profile(@RequestParam(value="userId",required= true)int userId) { 
		ModelAndView model = new ModelAndView("showProfile");
		
		User tmpUser = userDao.findById(userId);
		model.addObject("User", tmpUser) ;
		
		model.addObject("Profile", profileDao.findByEmail(tmpUser.getEmail()));
		
		return model;
	}
	
	/**
	 * Gets the profile which belongs to the actually logged in user
	 * 
	 * @param user is needed to get the right profile
	 * @return profile of the actually logged in user
	 */
	private long getUsersProfile(Principal user) {
		User tmpUser = userDao.findByEmail(user.getName());
		long profileId = tmpUser.getId();
		return profileId;
	}
	
}
