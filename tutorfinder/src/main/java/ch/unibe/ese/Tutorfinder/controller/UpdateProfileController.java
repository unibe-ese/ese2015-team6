package ch.unibe.ese.Tutorfinder.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidProfileException;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidTimetableException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Row;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateSubjectsService;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateTimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the edit or update profile process
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
// TODO Refactor this class, has some code smells
@Controller
public class UpdateProfileController {

	@Autowired
	UpdateSubjectsService updateSubjectsService;
	@Autowired
	UpdateTimetableService updateTimetableService;
	@Autowired
	UserService userService;
	@Autowired
	TimetableService timetableService;
	@Autowired
	ProfileService profileService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	PrepareFormService prepareFormService;

	/**
	 * Maps the /editProfile page to the {@code updateProfile.jsp}.
	 * 
	 * @param user
	 *            actually logged in user, is used to get the users profile
	 *            information and shows it to the user to allow editing the
	 *            information.
	 * @return ModelAndView for Spring framework with the users editable
	 *         profile.
	 */
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfile(Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");

		model = prepareFormService.prepareForm(user, model);
		return model;
	}

	/**
	 * Handles users form input with let it validate by the
	 * {@code UpdarProfileForm} class and passing it to the
	 * {@code UpdateProfileServiceImpl} class which saves the new information to
	 * the database.
	 * 
	 * @param user
	 *            actually logged in user, is used to get the users profile
	 *            information and shows it to the user to allow editing the
	 *            information.
	 * @param updateProfileForm
	 *            class to validate the users form input.
	 * @param result
	 * @param redirectAttributes
	 * @return ModelAndView for Spring framework with the users new and editable
	 *         profile.
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(Principal user, @Valid UpdateProfileForm updateProfileForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView model;
		if (!result.hasErrors()) {
			try {
				profileService.saveFrom(updateProfileForm, userService.getUserByPrincipal(user));
				model = new ModelAndView("updateProfile");
				// TODO show success message to the user
			} catch (InvalidProfileException e) {
				model = new ModelAndView("updateProfile");
				model.addObject("page_error", e.getMessage());
				// TODO show error massage to the user
			}
		} else {
			model = new ModelAndView("updateProfile");
			model.addObject("User", userService.getUserByPrincipal(user));
			model.addObject("updateSubjectsForm",
					prepareFormService.getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(userService.getUserByPrincipal(user))));
			return model;
		}
		model = prepareFormService.prepareForm(user, model);
		model.addObject("updateProfileForm", updateProfileForm);

		return model;
	}

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(Principal user, @RequestParam("file") MultipartFile file) {
		ModelAndView model;
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("user.dir");
				User tmpUser = userService.getUserByPrincipal(user);
				File dir = new File(rootPath + File.separator + "src" + File.separator + "main" + File.separator
						+ "webapp" + File.separator + "img" + File.separator + "profPic");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + tmpUser.getId() + ".png");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				model = new ModelAndView("updateProfile");
				// TODO show success massage to the user
			} catch (Exception e) {
				model = new ModelAndView("updateProfile");
				model.addObject("page_error", e.getMessage());
			}
		} else {
			model = new ModelAndView("updateProfile");
			// TODO show error massage to the user
		}

		model = prepareFormService.prepareForm(user, model);
		return model;
	}

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

	@RequestMapping(value = "/updateTimetable", method = RequestMethod.POST)
	public ModelAndView updateTimetable(@Valid UpdateTimetableForm updateTimetableForm, BindingResult result,
			Principal user) {
		ModelAndView model = new ModelAndView("updateProfile");
		if (!result.hasErrors()) {
			try {
				updateTimetableService.saveFrom(updateTimetableForm, user);
			} catch (InvalidTimetableException e) {
				// TODO Handling
				System.err.println("Timetable error");
			}
		}

		model = prepareFormService.prepareForm(user, model);
		model.addObject("updateTimetableForm", updateTimetableForm);
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
