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

	@Override
	public ModelAndView prepareForm(Principal authUser, ModelAndView model) {
		User dbUser = userService.getUserByPrincipal(authUser);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(dbUser)));
		model.addObject("updateProfileForm", getFormWithValues(authUser));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("User", dbUser);
		return model;
	}

	@Override
	public UpdateTimetableForm getUpdateTimetableFormWithValues(User dbUser) {
		UpdateTimetableForm tmpForm = new UpdateTimetableForm();
		Boolean[][] tmpMatrix = new Boolean[ConstantVariables.TIMESLOTS][ConstantVariables.DAYS];
		for (Boolean[] row : tmpMatrix)
			Arrays.fill(row, false);
		List<Timetable> tempList = timetableService.findAllByUser(dbUser);
		for (Timetable element : tempList) {
			int day = element.getDay().getValue() - 1;
			int timeslot = element.getTime();
			tmpMatrix[timeslot][day] = true;
		}
		tmpForm.setTimetable(tmpMatrix);
		return tmpForm;
	}

	@Override
	public UpdateSubjectsForm getUpdateSubjectWithValues(ArrayList<Subject> subjectList) {
		UpdateSubjectsForm tempForm = new UpdateSubjectsForm();
		List<Row> rowList = new ArrayList<Row>();
		for (Subject subject : subjectList) {
			rowList.add(new Row(subject.getName(), subject.getGrade()));
		}
		tempForm.setRows(rowList);
		return tempForm;
	}

	@Override
	public UpdateProfileForm getFormWithValues(Principal user) {
		UpdateProfileForm tmpForm = new UpdateProfileForm();
		tmpForm.setFirstName((userService.getUserByPrincipal(user)).getFirstName());
		tmpForm.setLastName((userService.getUserByPrincipal(user)).getLastName());
		tmpForm.setBiography(getUsersProfile(user).getBiography());
		tmpForm.setRegion(getUsersProfile(user).getRegion());
		tmpForm.setWage(getUsersProfile(user).getWage());

		return tmpForm;
	}

	@Override
	public Profile getUsersProfile(Principal user) {
		User tmpUser = userService.getUserByPrincipal(user);
		Profile tmpProfile = profileService.getProfileById(tmpUser.getId());

		return tmpProfile;
	}

	@Override
	public ModelAndView prepareForm(Principal user, ModelAndView model, UpdateSubjectsForm updateSubjectsForm) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", updateSubjectsForm);
		model.addObject("updateProfileForm", getFormWithValues(user));
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("User", dbUser);
		return model;
	}

	@Override
	public ModelAndView prepareForm(Principal user, ModelAndView model, UpdateProfileForm updateProfileForm) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(dbUser)));
		model.addObject("updateProfileForm", updateProfileForm);
		model.addObject("updateTimetableForm", getUpdateTimetableFormWithValues(dbUser));
		model.addObject("User", dbUser);
		return model;
	}

	@Override
	public ModelAndView prepareForm(Principal user, ModelAndView model, UpdateTimetableForm updateTimetableForm) {
		User dbUser = userService.getUserByPrincipal(user);
		model.addObject("updateSubjectsForm", getUpdateSubjectWithValues(subjectService.getAllSubjectsByUser(dbUser)));
		model.addObject("updateProfileForm", getFormWithValues(user));
		model.addObject("updateTimetableForm", updateTimetableForm);
		model.addObject("User", dbUser);
		return model;
	}

}
