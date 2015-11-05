package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.SignupForm;
import ch.unibe.ese.Tutorfinder.controller.service.RegisterService;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the login/logout process
 * 
 * @author Antonio, Florian, Nicola, Lukas
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
	
	@Autowired
	RegisterService registerService;
	
	@RequestMapping(value={"/","/home"})
	public String home() {
		return "redirect:login";
	}
    
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	ModelAndView model;    	
    	if (!result.hasErrors()) {
            try {
            	registerService.saveFrom(signupForm);
            	model = new ModelAndView("signupCompleted");
            	

            } catch (InvalidEmailException e) {
            	result.rejectValue("email", "", e.getMessage());
            	model = new ModelAndView("login");
            	model.addObject("loginBoxVisibility", "hidden");
        		model.addObject("registerBoxVisibility", "visible");
            }
            catch (InvalidUserException e) {
            	result.reject("page_error", e.getMessage());
            	model = new ModelAndView("login");
            	model.addObject("loginBoxVisibility", "hidden");
        		model.addObject("registerBoxVisibility", "visible");
            }
            //TODO exception for invalid password with message

        } else {
        	model = new ModelAndView("login");
        	model.addObject("loginBoxVisibility", "hidden");
    		model.addObject("registerBoxVisibility", "visible");
        }   	
    	return model;
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
    	return "redirect:/login?register";
    }
    
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, @RequestParam(value = "register", required = false) String register) {

		String loginBoxVis = "visible";
		String registerBoxVis = "hidden";
		
		ModelAndView model = new ModelAndView("login");
		if (error != null) {
			model.addObject("error", "Invalid username or password!");
		}
		
		if (register != null){
			loginBoxVis = "hidden";
			registerBoxVis = "visible";
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.addObject("loginBoxVisibility", loginBoxVis);
		model.addObject("registerBoxVisibility", registerBoxVis);
		model.addObject("loginUrl", "/login");
		model.addObject("signupForm", new SignupForm());

		return model;
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success() {
		return "redirect:findTutor";

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

		ModelAndView model = new ModelAndView("403");
		if (user != null) {
			model.addObject("msg", "Name: " + user.getName());
		}
		return model;

	}
}
