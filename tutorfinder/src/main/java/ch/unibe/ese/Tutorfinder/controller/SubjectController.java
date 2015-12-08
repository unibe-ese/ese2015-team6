package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Row;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.Timetable;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update Subjects process
 * 
 * @version 1.0
 *
 */
@Controller
public class SubjectController {
	
	@Autowired
	SubjectService subjectService;
	@Autowired
	PrepareFormService prepareFormService;
	@Autowired
	UserService userService;
	@Autowired
	TimetableService timetableService;

	/**
	 * Handles the action to save the subjects which currently are in the form.
	 * 
	 * @param authUser {@link Principal}
	 * @param updateSubjectsForm holds the updated information for the {@link Subject}'s entry in the database
	 * @param result
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/editSubjects", params = "save", method = RequestMethod.POST)
	public ModelAndView updateSubjects(Principal authUser, @Valid UpdateSubjectsForm updateSubjectsForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView("redirect:/editProfile");
		List<Timetable> tmpList = timetableService.findAllByUser(userService.getUserByPrincipal(authUser));
		if (tmpList != null && tmpList.isEmpty()) {
			redirectAttributes.addFlashAttribute("switch", true); }
		if (!result.hasErrors()) {
			try {
				subjectService.saveFrom(updateSubjectsForm, authUser);
				redirectAttributes.addFlashAttribute("subject_msg", "Your subjects have been updated");
			
			} catch (InvalidSubjectException e) {
				result.reject("error", e.getMessage());
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateSubjectsForm", result);
			}
			redirectAttributes.addFlashAttribute("updateSubjectsForm", updateSubjectsForm);
		}
		return model;
	}

	/**
	 * Handles action to add a new {@link Row} to the ArrayList of rows and
	 * preserves the currently entered values
	 * 
	 * @param updateSubjectsForm
	 * @param user
	 *            {@link Principal}
	 * @return
	 */
	@RequestMapping(value = "/editSubjects", params = "addRow")
	public ModelAndView addRow(@Valid UpdateSubjectsForm updateSubjectsForm, Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");
		updateSubjectsForm.getRows().add(new Row());
		model = prepareFormService.prepareForm(user, model);
		model.addObject("updateSubjectsForm", updateSubjectsForm);
		model.addObject("updateProfileForm", prepareFormService.getFormWithValues(user));
		return model;
	}

	/**
	 * Handles action to remove a {@link Row} by the index passed as the value
	 * of the clicked button while preserving the other values
	 * 
	 * @param updateSubjectsForm
	 * @param req
	 *            used to get RowId of Row to remove
	 * @param user
	 *            {@link Principal}
	 * @return
	 */
	@RequestMapping(value = "/editSubjects", params = "remRow")
	public ModelAndView removeRow(@Valid UpdateSubjectsForm updateSubjectsForm, final HttpServletRequest req,
			Principal authUser) {
		ModelAndView model = new ModelAndView("updateProfile");
		final Integer rowId = Integer.valueOf(req.getParameter("remRow"));
		updateSubjectsForm.getRows().remove(rowId.intValue());
		model = prepareFormService.prepareForm(authUser, model);
		model.addObject("updateSubjectsForm", updateSubjectsForm);
		model.addObject("updateProfileForm", prepareFormService.getFormWithValues(authUser));
		return model;
	}
	
	/**
	 * Converts empty fields to null values
	 * 
	 * @param binder
	 */
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
