package ch.unibe.ese.Tutorfinder.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
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
import ch.unibe.ese.Tutorfinder.controller.service.UpdateProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateSubjectsService;
import ch.unibe.ese.Tutorfinder.controller.service.UpdateTimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

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
	UpdateProfileService updateProfileService;
	@Autowired
	UpdateSubjectsService updateSubjectsService;
	@Autowired
	UpdateTimetableService updateTimetableService;
	@Autowired
	UserService userService;

	@Autowired
	ProfileDao profileDao;
	@Autowired
	SubjectDao subjectDao;
	@Autowired
	TimetableDao timetableDao;

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

		model = prepareForm(user, model);
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
				updateProfileService.saveFrom(updateProfileForm, user);
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
					getUpdateSubjectWithValues(subjectDao.findAllByUser(userService.getUserByPrincipal(user))));
			return model;
		}
		model = prepareForm(user, model, updateProfileForm);

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

		model = prepareForm(user, model);
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
			model = prepareForm(user, model, updateSubjectsForm);
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
		model = prepareForm(user, model, updateSubjectsForm);
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
		model = prepareForm(user, model, updateSubjectsForm);
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

		model = prepareForm(user, model, updateTimetableForm);
		return model;
	}

	private ModelAndView prepareForm(Principal user, ModelAndView model, UpdateTimetableForm updateTimetableForm) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectDao.findAllByUser(dbUser)));
		model.addObject("updateProfileForm", getFormWithValues(user));
		model.addObject("updateTimetableForm", updateTimetableForm);
		model.addObject("User", dbUser);
		return model;
	}

	/**
	 * Gets an form with the users new information
	 * 
	 * @param user
	 *            {@link Principal}
	 * @return form with the users input values
	 */
	private UpdateProfileForm getFormWithValues(Principal user) {
		UpdateProfileForm tmpForm = new UpdateProfileForm();
		tmpForm.setFirstName((userService.getUserByPrincipal(user)).getFirstName());
		tmpForm.setLastName((userService.getUserByPrincipal(user)).getLastName());
		tmpForm.setBiography(getUsersProfile(user).getBiography());
		tmpForm.setRegion(getUsersProfile(user).getRegion());
		tmpForm.setWage(getUsersProfile(user).getWage());

		return tmpForm;
	}

	/**
	 * Gets the profile which belongs to the actually logged in user
	 * 
	 * @param user
	 *            {@link Principal} is needed to get the right profile
	 * @return profile of the actually logged in user
	 */
	private Profile getUsersProfile(Principal user) {
		User tmpUser = userService.getUserByPrincipal(user);
		Profile tmpProfile = profileDao.findOne(tmpUser.getId());

		return tmpProfile;
	}

	/**
	 * Injects needed objects into a given {@link ModelAndView} and gets the
	 * {@link Subjects} for the {@link UpdateSubjectsForm} from the DB
	 * essentially discarding the form's current state.
	 * 
	 * @param user
	 *            {@link Principal}
	 * @param model
	 *            {@link ModelAndView}
	 * @return
	 */
	private ModelAndView prepareForm(Principal user, ModelAndView model) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectDao.findAllByUser(dbUser)));
		model.addObject("updateProfileForm", getFormWithValues(user));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("User", dbUser);
		return model;
	}

	private UpdateTimetableForm getUpdateTimetableFormWithValues(User dbUser) {
		UpdateTimetableForm tmpForm = new UpdateTimetableForm();
		Boolean[][] tmpMatrix = new Boolean[ConstantVariables.TIMESLOTS][ConstantVariables.DAYS];
		for (Boolean[] row : tmpMatrix)
			Arrays.fill(row, false);
		List<Timetable> tempList = timetableDao.findAllByUser(dbUser);
		for (Timetable element : tempList) {
			int day = element.getDay().getValue() - 1;
			int timeslot = element.getTime();
			tmpMatrix[timeslot][day] = true;
		}
		tmpForm.setTimetable(tmpMatrix);
		return tmpForm;
	}

	/**
	 * Injects needed objects into a given {@link ModelAndView} while preserving
	 * the current rows of the {@link UpdateSubjectsForm}
	 * 
	 * @param user
	 *            {@link Principal}
	 * @param model
	 *            {@link ModelAndView}
	 * @param updateSubjectsForm
	 *            {@link UpdateSubjectsForm}
	 * @return {@link ModelAndView} ready for return
	 */
	private ModelAndView prepareForm(Principal user, ModelAndView model, UpdateSubjectsForm updateSubjectsForm) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", updateSubjectsForm);
		model.addObject("updateProfileForm", getFormWithValues(user));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("User", dbUser);
		return model;
	}

	/**
	 * Injects needed objects into a given {@link ModelAndView} while preserving
	 * the current {@link UpdateProfileForm}
	 * 
	 * @param user
	 *            {@link Principal}
	 * @param model
	 *            {@link ModelAndView}
	 * @param updateSubjectsForm
	 *            {@link UpdateSubjectsForm}
	 * @return {@link ModelAndView} ready for return
	 */
	private ModelAndView prepareForm(Principal user, ModelAndView model, UpdateProfileForm updateProfileForm) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectDao.findAllByUser(dbUser)));
		model.addObject("updateProfileForm", getFormWithValues(user));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("User", dbUser);
		return model;
	}

	/**
	 * Converts an ArrayList of Subjects into a {@link UpdateSubjectsForm}
	 * filled with rows containing all the information from the given subjects
	 * 
	 * @param subjectList
	 *            Subject array list (from db)
	 * @return UpdateSubjectForm filled with rows
	 */
	// TODO Move to service
	private UpdateSubjectsForm getUpdateSubjectWithValues(ArrayList<Subject> subjectList) {
		UpdateSubjectsForm tempForm = new UpdateSubjectsForm();
		List<Row> rowList = new ArrayList<Row>();
		for (Subject subject : subjectList) {
			rowList.add(new Row(subject.getName(), subject.getGrade()));
		}
		tempForm.setRows(rowList);
		return tempForm;
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
