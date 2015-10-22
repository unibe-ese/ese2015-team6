package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the login/logout process
 * 
 * @author Nicola
 *
 */
@Controller
public class LoginController {

	/**
	 * Maps the /login page to the login form (login.jsp) and provides optional
	 * parameters for displaying messages
	 * 
	 * @param error displays invalid credentials message
	 * @param logout displays successful logout message
	 * @return ModelAndView for Springframework
	 */


	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView("html/login");
		if (error != null) {
			model.addObject("error", "Invalid username or password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.addObject("loginUrl", "/login");

		return model;
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success() {
		ModelAndView model = new ModelAndView("success");
		model.addObject("logoutUrl", "/login?logout");
		return model;

	}

	/**
	 * Handles logout of the user by invalidating his session.
	 * 
	 * @param request
	 * @param response
	 * @return redirection to login screen
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	/**
	 * Displays custom access denied page with optional message displaying username
	 * @param user authenticated user object
	 * @return
	 */
	@RequestMapping(value = "/403", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView accesssDenied(Principal user) {

		ModelAndView model = new ModelAndView("html/403");
		if (user != null) {
			model.addObject("msg", "Name: " + user.getName());
		}
		return model;

	}
}
