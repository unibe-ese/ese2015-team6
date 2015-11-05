package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.NoTutorsForSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.FindTutorForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

@Controller
public class FindTutorController {
	
	@Autowired
	FindTutorService findTutorService;
	
	@Autowired	UserDao userDao;
	
    @RequestMapping(value = "/findTutor", method = RequestMethod.GET)
    public ModelAndView findTutor(Principal user) {
    	ModelAndView model = new ModelAndView("findTutor");
    	model.addObject("findTutorForm", new FindTutorForm());
    	model.addObject("User", userDao.findByEmail(user.getName()));
        return model;
    }
    
    @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
    public ModelAndView create(Principal user, @Valid FindTutorForm findTutorForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	ModelAndView model;
    	
    	if (!result.hasErrors()) {
            try {
            	
            	model = new ModelAndView("searchResults");
            	model.addObject("Query", findTutorForm.getSubject());
            	model.addObject("Result", findTutorService.getSubjectFrom(findTutorForm));
            } 
            
            catch (NoTutorsForSubjectException e) {
            	model = new ModelAndView("findTutor");
            	model.addObject("page_error", e.getMessage());
            }

        } else {
        	model = new ModelAndView("findTutor");
        }   	
    	model.addObject("User", userDao.findByEmail(user.getName()));
    	return model;
    }
    
}
