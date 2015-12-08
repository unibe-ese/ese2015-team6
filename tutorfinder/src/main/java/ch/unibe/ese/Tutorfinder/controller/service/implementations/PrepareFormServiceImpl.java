package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.Row;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.PasswordConfirmationForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateProfileForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.controller.service.PrepareFormService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Service
public class PrepareFormServiceImpl implements PrepareFormService {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TimetableService timetableService;

	public PrepareFormServiceImpl() {
		super();
	}

	/**
	 * Constructor for testing purposes
	 * @param userService
	 * @param profileService
	 * @param subjectService
	 * @param timetableService
	 */
	@Autowired
	public PrepareFormServiceImpl(UserService userService, ProfileService profileService, SubjectService subjectService, TimetableService timetableService) {
		this.userService = userService;
		this.profileService = profileService;
		this.subjectService = subjectService;
		this.timetableService = timetableService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModelAndView prepareForm(Principal authUser, ModelAndView model) {
		assert(authUser != null && model != null);
		
		User dbUser = userService.getUserByPrincipal(authUser);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(dbUser)));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		if (dbUser.getRole().equals(ConstantVariables.STUDENT)) {
			model.addObject("passwordConfirmationForm", new PasswordConfirmationForm());
		}
		model.addObject("authUser", dbUser);
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UpdateTimetableForm getUpdateTimetableFormWithValues(User dbUser) {
		assert(dbUser != null);
		
		UpdateTimetableForm tmpForm = new UpdateTimetableForm();
		Boolean[][] tmpMatrix = new Boolean[ConstantVariables.TIMESLOTS][ConstantVariables.DAYS];
		for (Boolean[] row : tmpMatrix)
			Arrays.fill(row, false);
		List<Timetable> tmpList = timetableService.findAllByUser(dbUser);
		for (Timetable element : tmpList) {
			int day = element.getDay().getValue() - 1;
			int timeslot = element.getTime();
			tmpMatrix[timeslot][day] = true;
		}
		tmpForm.setTimetable(tmpMatrix);
		return tmpForm;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UpdateSubjectsForm getUpdateSubjectWithValues(ArrayList<Subject> subjectList) {
		assert(subjectList != null);
		
		UpdateSubjectsForm tmpForm = new UpdateSubjectsForm();
		List<Row> rowList = new ArrayList<Row>();
		for (Subject subject : subjectList) {
			rowList.add(new Row(subject.getName(), subject.getGrade()));
		}
		tmpForm.setRows(rowList);
		return tmpForm;
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public UpdateProfileForm getFormWithValues(Principal authUser) {
		assert(authUser != null);
		User user = userService.getUserByPrincipal(authUser);
		Profile profile = getUsersProfile(authUser);
		UpdateProfileForm tmpForm = new UpdateProfileForm();
		tmpForm.setFirstName(user.getFirstName());
		tmpForm.setLastName(user.getLastName());
		tmpForm.setBiography(profile.getBiography());
		tmpForm.setRegion(profile.getRegion());
		tmpForm.setWage(profile.getWage());
		tmpForm.setUniversity(profile.getUniversity());
		tmpForm.setLanguage(profile.getLanguage());

		return tmpForm;
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public ModelAndView prepareModelByUserId(Principal authUser, long userId, ModelAndView model) {
		User tmpAuthUser = userService.getUserByPrincipal(authUser);
		model.addObject("authUser", tmpAuthUser);
		
		User tmpUser = userService.getUserById(userId);
		model.addObject("DisplayedUser", tmpUser);
		model.addObject("Subjects", subjectService.getAllSubjectsByUser(tmpUser));
		model.addObject("Profile", profileService.getProfileByEmail(tmpUser.getEmail()));
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Profile getUsersProfile(Principal user) {
		assert(user != null);
		
		User tmpUser = userService.getUserByPrincipal(user);
		Profile tmpProfile = profileService.getProfileById(tmpUser.getId());

		return tmpProfile;
	}

}
