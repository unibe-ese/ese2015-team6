package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.exceptions.NoTutorsForSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;

@Controller
public class FindTutorController {

	@Autowired
	FindTutorService findTutorService;
	@Autowired
	UserService userService;

	@RequestMapping(value = "/findTutor", method=RequestMethod.GET)
	public ModelAndView findTutor(Principal authUser, @RequestParam(value = "q", required = false) String query) {
		ModelAndView model = new ModelAndView("findTutor");
		if (query != null && !query.equals("")) {
			try {
				model.addObject("Result", findTutorService.getSubjectsFrom(query));
			} catch (NoTutorsForSubjectException e) {
				model = new ModelAndView("findTutor");
			}
		}
		model.addObject("findTutorForm", new FindTutorForm());
		User tmpAuthUser = userService.getUserByPrincipal(authUser);
		model.addObject("authUser", tmpAuthUser);
		return model;
	}
	
	@RequestMapping(value="/submit", method=RequestMethod.POST)
	public String submit(@Valid FindTutorForm form, BindingResult result) {
		return "redirect:/findTutor?q=" + form.getSubject();
	}

}
