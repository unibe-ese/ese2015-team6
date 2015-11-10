package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.pojos.Row;
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
	UserService userService;
	@Autowired
	ProfileService profileService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	TimetableService timetableService;

	// Constructor for testing purposes
	@Autowired
	public PrepareFormServiceImpl(UserService userService, ProfileService profileService, SubjectService subjectService, TimetableService timetableService) {
		this.userService = userService;
		this.profileService = profileService;
		this.subjectService = subjectService;
		this.timetableService = timetableService;
	}


	@Override
	public ModelAndView prepareForm(Principal authUser, ModelAndView model) {
		assert(authUser != null && model != null);
		
		User dbUser = userService.getUserByPrincipal(authUser);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(dbUser)));
		model.addObject("updateProfileForm", getFormWithValues(authUser));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("authUser", dbUser);
		return model;
	}


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
	 * takes an ArrayList of Subject Obj. and creates an UpdateSubjectsForm containing 
	 * each subject name and corresponding grade
	 * @param Arraylist of Subjects that should be added to the form
	 * @return UpdateSubjectForm containing one or multiple row obj. with subject name and grade 
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
	 * reads data that belongs to the currently logged in user (Principal) from
	 * the database and writes it into an UpdateProfileForm
	 * @param Principal of an user
	 * @return UpdateProfileForm filled with profile info from the database
	 */
	@Override
	public UpdateProfileForm getFormWithValues(Principal user) {
		assert(user != null);
		UpdateProfileForm tmpForm = new UpdateProfileForm();
		tmpForm.setFirstName((userService.getUserByPrincipal(user)).getFirstName());
		tmpForm.setLastName((userService.getUserByPrincipal(user)).getLastName());
		tmpForm.setBiography(getUsersProfile(user).getBiography());
		tmpForm.setRegion(getUsersProfile(user).getRegion());
		tmpForm.setWage(getUsersProfile(user).getWage());

		return tmpForm;
	}
	
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
	 * Returns the profile corresponding to a Principal 
	 * @param Principal of which the profile should be returned
	 * @return profile obj. that belongs to the Principal 
	 */
	@Override
	public Profile getUsersProfile(Principal user) {
		assert(user != null);
		
		User tmpUser = userService.getUserByPrincipal(user);
		Profile tmpProfile = profileService.getProfileById(tmpUser.getId());

		return tmpProfile;
	}

}
