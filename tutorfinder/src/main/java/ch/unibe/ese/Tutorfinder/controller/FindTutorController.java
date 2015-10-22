package ch.unibe.ese.Tutorfinder.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidUserException;
import ch.unibe.ese.Tutorfinder.controller.pojos.FindTutorForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;

@Controller
public class FindTutorController {
	
	@Autowired
	FindTutorService findTutorService;
	
    @RequestMapping(value = "/findTutor", method = RequestMethod.GET)
    public ModelAndView findTutor() {
    	ModelAndView model = new ModelAndView("html/findTutor");
    	model.addObject("findTutorForm", new FindTutorForm());
        return model;
    }
    
    @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
    public ModelAndView create(@Valid FindTutorForm findTutorForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	ModelAndView model;    	
    	if (!result.hasErrors()) {
            try {
            	findTutorService.getUsersFrom(findTutorForm);
            	model = new ModelAndView("html/searchResults");

            } 
            
            catch (InvalidUserException e) {
            	model = new ModelAndView("html/findTutor");
            	model.addObject("page_error", e.getMessage());
            }

        } else {
        	model = new ModelAndView("html/findTutor");
        }   	
    	return model;
    }
    
}
