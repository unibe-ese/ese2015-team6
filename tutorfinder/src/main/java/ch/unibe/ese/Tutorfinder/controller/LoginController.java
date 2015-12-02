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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidEmailException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.SignupForm;
import ch.unibe.ese.Tutorfinder.controller.service.RegisterService;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the login/logout and register process.
 * 
 * @version 1.0.1
 *
 */
@Controller
public class LoginController {

	@Autowired
	RegisterService registerService;
	@Autowired	
	UserDao userDao;
	
	/**
	 * Constructor for testing purposes
	 * @param registerService
	 */
	@Autowired
	public LoginController(RegisterService registerService) {
		this.registerService = registerService;
	}

	/**
	 * Defines the SignupForm as a ModelAttribute
	 */
	@ModelAttribute("signupForm")
	public SignupForm getSignupForm() {
		return new SignupForm();
	}

	/**
	 * Redirects the {@code /} and {@code /home} to the 
	 * {@code login.html} page
	 * @return redirection to login
	 */
	@RequestMapping(value={"/","/home"})
	public String home() {
		return "redirect:login";
	}
    
	/**
	 * Redirects the {@code /register} to the {@code login.html} page
	 * 
	 * @return redirection to login
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "redirect:/login?register";
	}

	/**
	 * Redirects the {@code /success} to the {@code findTutor.html} page
	 * 
	 * @return redirection to login
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success() {
		return "redirect:findTutor";
	
	}

	/**
	 * Maps the /login page to the login form (login.html) and provides optional
	 * parameters for displaying messages
	 * 
	 * @param error displays invalid credentials message
	 * @param logout displays successful logout message
	 * @return ModelAndView for Spring Framework
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(Principal authUser,
							  @RequestParam(value = "error", required = false) String error,
							  @RequestParam(value = "logout", required = false) String logout,
							  @RequestParam(value = "register", required = false) String register,
							  @RequestParam(value = "success", required = false) String success) {
		ModelAndView model = new ModelAndView("login");
		if (authUser!= null) {
			model = new ModelAndView("redirect:/findTutor");
			return model;
		}
		if (error != null) {
			model.addObject("error", "Invalid username or password!");
		}
	
		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully");
		}
		
		if (register != null) {
			model.addObject("switch", true);
		}
		
		if (success != null) {
			model.addObject("msg", "Registration complete. Please login below");
		}
		model.addObject("loginUrl", "/login");
		
		return model;
	}

	/**
     * Loads the {@link User}'s input from the {@link SignupForm} and
     * creates with this information a new entry in the database.
     * After this is happens, it is possible for the user to login and
     * he has access to the sites corresponding to his role.
     * Redirects the user after the registration to a {@code signupCompleted.html}
     * page to show the successful creation of a new login.
     * 
     * @param signupForm holds the information needed to create an new {@link User}
     * @param result
     * @param redirectAttributes
     * @return if everything is valid a {@code signupCompleted.html} page, else again 
     * 			the login page with the necessary error messages
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("signupForm") SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	if (!result.hasErrors()) {
            try {
            	registerService.saveFrom(signupForm);
            	return "redirect:/login?success";

            } catch (InvalidEmailException e) {
            	result.rejectValue("email", "", e.getMessage());
            }
    	}
    	redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.signupForm", result);
    	redirectAttributes.addFlashAttribute("signupForm", signupForm);
    	return "redirect:/login?register";
    	
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
}
