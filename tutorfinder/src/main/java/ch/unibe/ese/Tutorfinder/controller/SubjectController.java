package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

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
import ch.unibe.ese.Tutorfinder.controller.service.UpdateSubjectsService;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update Subjects process
 * 
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Controller
public class SubjectController {
	
	@Autowired
	UpdateSubjectsService updateSubjectsService;
	@Autowired
	PrepareFormService prepareFormService;

	/**
	 * Handles the action to save the subjects which currently are in the form.
	 * 
	 * @param user
	 * @param updateSubjectsForm
	 * @param result
	 * @param redirectAttributes
	 * @return
	 */
	// FIXME no error message show when to subjects are named equal, only the
	// first one is saved but loaded are both after
	// press on the save button, but when canceled and go again on editProfile
	// page only one is saved
	// Futhermore allowed subject after two same named are not saved!
	@RequestMapping(value = "/editSubjects", params = "save", method = RequestMethod.POST)
	public ModelAndView updateSubjects(Principal user, @Valid UpdateSubjectsForm updateSubjectsForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView("updateProfile");

		if (!result.hasErrors()) {
			try {
				updateSubjectsService.saveFrom(updateSubjectsForm, user);
				// TODO show success message to the user
			} catch (InvalidSubjectException e) {
				result.reject("error", e.getMessage());
				model = new ModelAndView("updateProfile");
			}
			model = prepareFormService.prepareForm(user, model);
			model.addObject("updateSubjectsForm", updateSubjectsForm);
		} else {
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
			Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");
		final Integer rowId = Integer.valueOf(req.getParameter("remRow"));
		updateSubjectsForm.getRows().remove(rowId.intValue());
		model = prepareFormService.prepareForm(user, model);
		model.addObject("updateSubjectsForm", updateSubjectsForm);
		return model;
	}
	
	/**
	 * Converts empty fields to null values
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
