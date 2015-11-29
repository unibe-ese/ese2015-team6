package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import javax.swing.SortOrder;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.NoTutorsForSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.util.SortCriteria;

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

	FindTutorFilterForm filterForm;

	/**
	 * Defines and initializes the findTutorFilterForm as a ModelAttribute and prepares the comparator with default settings
	 */
	@ModelAttribute("findTutorFilterForm")
	private FindTutorFilterForm getFindTutorFilterForm() {
		if (filterForm == null) {
			filterForm = new FindTutorFilterForm();
			filterForm.setCriteria(SortCriteria.RATING);
			filterForm.setOrder(SortOrder.DESCENDING);
			findTutorService.generateComparatorFrom(filterForm);
		}
		return filterForm;
	}

	/**
	 * Maps the /findTutor pages to the {@code findTutor.html} view.
	 * 
	 * @param authUser
	 *            {@link Principal}
	 * @param query
	 *            {@code String} for searching for matching {@link Subject}s
	 * @return {@link ModelAndView} with links to the corresponding
	 *         {@link Tutor}s {@link Profile}s
	 */
	@RequestMapping(value = "/findTutor", method = RequestMethod.GET)
	public ModelAndView findTutor(Principal authUser, @RequestParam(value = "q", required = false) String query) {
		ModelAndView model = new ModelAndView("findTutor");
		String action = "submit";
		if (query != null && !query.equals("")) {
			try {
				action = "submit?q=" + query;
				model.addObject("Result", findTutorService.getSubjectsSorted(query));
			} catch (NoTutorsForSubjectException e) {
				model = new ModelAndView("findTutor");
			}
		}
		model.addObject("formaction", action);
		return model;
	}

	/**
	 * Starts the query for an {@link Subject} named equals to the input
	 * {@code String} or which has an substring that matches it. When a subject
	 * is found, it will be shown under the search engine.
	 * 
	 * @param form
	 *            holds the query {@code String}
	 * @param result
	 * @return redirection to /findTutor with the query {@code String}
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(@RequestParam(value = "q", required = false) String query,
			@Valid @ModelAttribute("findTutorFilterForm") FindTutorFilterForm form, BindingResult result,
			RedirectAttributes redirect) {
		assert !result.hasErrors() : "The form has an error where it shouldn't have any\n"+result.getAllErrors();
		findTutorService.generateComparatorFrom(form);
		this.filterForm = form;
		redirect.addFlashAttribute("org.springframework.validation.BindingResult.findTutorFilterForm", result);
		if (query != null)
			return "redirect:findTutor?q=" + query;
		return "redirect:findTutor";
	}

}
