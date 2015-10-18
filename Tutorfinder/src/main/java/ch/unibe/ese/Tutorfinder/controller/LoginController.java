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

@Controller
public class LoginController {
	@RequestMapping (value = "/login", method = RequestMethod.GET)
	public ModelAndView login( 
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout){
		
		ModelAndView model = new ModelAndView();
		  if (error != null) {
			model.addObject("error", "Invalid username and password!");
		  }

		  if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		  }
		  model.setViewName("login");
		  
		  model.addObject("loginUrl", "/login");

		  return model;
	}
	
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success() {
		ModelAndView model = new ModelAndView();
		model.setViewName("success");
		model.addObject("logoutUrl", "/login?logout");
		return model;
		
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied(Principal user) {

	  ModelAndView model = new ModelAndView();
	  if (user != null) {
		  model.addObject("msg", "Name: " + user.getName());
	  }
	  
	  model.setViewName("403");
	  return model;

	}

}
