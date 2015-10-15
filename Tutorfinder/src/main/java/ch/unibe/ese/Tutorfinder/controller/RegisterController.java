package ch.unibe.ese.Tutorfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.controller.pojos.SignupForm;
import ch.unibe.ese.controller.service.RegisterService;

@Controller
public class RegisterController {

	@Autowired
	RegisterService registerService;
	
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView index() {
    	ModelAndView model = new ModelAndView("register");
    	model.addObject("signupForm", new SignupForm());
        return model;
    }
}
