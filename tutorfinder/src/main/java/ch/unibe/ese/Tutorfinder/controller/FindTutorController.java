package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
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
@Scope("session")
public class FindTutorController {

	@Autowired
	private FindTutorService findTutorService;
	@Autowired
	private UserService userService;

	@Autowired
	private FindTutorFilterForm filterForm;

	@Autowired
	public FindTutorController(FindTutorService findTutorService, UserService userService,
			FindTutorFilterForm filterForm) {
		this.findTutorService = findTutorService;
		this.userService = userService;
		this.filterForm = filterForm;
	}

	/**
	 * Maps the /findTutor pages to the {@code findTutor.html} view.
	 * 
	 * @param authUser
	 *            {@link Principal}
	 * @param query
	 *            {@code String} for searching for matching {@link Subject}
	 * @return {@link ModelAndView} with links to the corresponding
	 *         {@link Tutor}s {@link Profile}s
	 */
	@RequestMapping(value = "/findTutor", method = RequestMethod.GET)
	public ModelAndView findTutor(Principal authUser, @RequestParam(value = "q", required = false) String query) {
		ModelAndView model = new ModelAndView("findTutor");
		String action = "submit";
		if (query != null && !query.equals("")) {
			action = "submit?q=" + query;
			findTutorService.generateComparatorFrom(filterForm);
			model.addObject("Result", findTutorService.getSubjectsSorted(query));
		}
		model.addObject("findTutorFilterForm", filterForm);
		model.addObject("formaction", action);
		model.addObject("user", userService.getUserByPrincipal(authUser));
		return model;
	}

	/**
	 * Starts the query for an {@link Subject} named equals to the input
	 * {@code String} or which has an substring that matches it. When a subject
	 * is found, it will be shown under the search engine.
	 * 
	 * @param form
	 *            holds the query {@code String}
	 * @param bindingResult
	 * @return redirection to /findTutor with the query {@code String}
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(@RequestParam(value = "q", required = false) String query, @Valid FindTutorFilterForm form,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		assert !bindingResult.hasErrors() : "The form has an error where it shouldn't have any\n" + bindingResult.getAllErrors();
		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.findTutorFilterForm", bindingResult);

		filterForm.setCriteria(form.getCriteria());
		filterForm.setOrder(form.getOrder());

		if (query != null)
			return "redirect:findTutor?q=" + query;
		return "redirect:findTutor";
	}

}
