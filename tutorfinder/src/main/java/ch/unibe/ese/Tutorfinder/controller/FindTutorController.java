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
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the search {@link Tutor} process
 * 
 * @version 1.0
 *
 */
@Controller
public class FindTutorController {

	@Autowired
	FindTutorService findTutorService;
	@Autowired
	UserService userService;

	/**
	 * Maps the /findTutor pages to the {@code findTutor.html} view.
	 * 
	 * @param authUser {@link Principal}
	 * @param query	{@code String} for searching for matching {@link Subject}s
	 * @return {@link ModelAndView} with links to the corresponding {@link Tutor}s {@link Profile}s
	 */
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
		return model;
	}
	
	/**
	 * Starts the query for an {@link Subject} named equals to the
	 * input {@code String} or which has an substring that matches it.
	 * When a subject is found, it will be shown under the search engine.
	 * 
	 * @param form holds the query {@code String}
	 * @param result
	 * @return redirection to /findTutor with the query {@code String}
	 */
	@RequestMapping(value="/submit", method=RequestMethod.POST)
	public String submit(@Valid FindTutorForm form, BindingResult result) {
		return "redirect:/findTutor?q=" + form.getSubject();
	}

}
