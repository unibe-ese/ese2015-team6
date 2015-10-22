package ch.unibe.ese.Tutorfinder.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.pojos.SignupForm;
import ch.unibe.ese.Tutorfinder.controller.service.RegisterService;
import ch.unibe.ese.Tutorfinder.controller.exceptions.*;

@Controller
public class RegisterController {

	@Autowired
	RegisterService registerService;
	
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView index() {
    	ModelAndView model = new ModelAndView("html/register");
    	model.addObject("signupForm", new SignupForm());
        return model;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	ModelAndView model;    	
    	if (!result.hasErrors()) {
            try {
            	registerService.saveFrom(signupForm);
            	model = new ModelAndView("html/signupCompleted");

            } catch (InvalidEmailException e) {
            	model = new ModelAndView("html/register");
            	model.addObject("email_error", e.getMessage());
            }
            catch (InvalidUserException e) {
            	model = new ModelAndView("html/register");
            	model.addObject("page_error", e.getMessage());
            }

        } else {
        	model = new ModelAndView("html/register");
        }   	
    	return model;
    }
}

