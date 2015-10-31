package ch.unibe.ese.Tutorfinder.controller.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Row;
import ch.unibe.ese.Tutorfinder.controller.pojos.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

/**
 * Service to save the information from the {@code UpdateProfileForm}
 * to the subject-table on the database. This service is used for
 * update the users subjects.
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
@Service
public class UpdateSubjectsServiceImpl implements UpdateSubjectsService {

	@Autowired
	UserDao userDao;
	@Autowired
	SubjectDao subjectDao;

	public UpdateSubjectsServiceImpl() {
	}

	/**
	 * Replaces all currently saved subjects for one tutor with the subjects
	 * currently in the form by deleting and re-adding them
	 */
	// FIXME Prohibit negative grades (limit to 1-6)
	@Transactional
	public UpdateSubjectsForm saveFrom(UpdateSubjectsForm updateSubjectsForm, Principal authUser)
			throws InvalidSubjectException {
		User user = userDao.findByEmail(authUser.getName());
		List<Subject> tmpSubjList = subjectDao.findAllByUser(user);
		subjectDao.delete(tmpSubjList);
		List<Subject> subjects = new ArrayList<Subject>();
		List<Row> rowList = updateSubjectsForm.getRows();

		// TODO Refactor: This could be in a separate class
		for (Row row : rowList) {
			String name = row.getSubject();
			if (name != null) {
				Subject subject = new Subject();
				subject.setUser(user);
				subject.setName(name);
				subject.setGrade(row.getGrade());
				subjects.add(subject);
			}
		}
		try {
			subjectDao.save(subjects);
		} catch (DataIntegrityViolationException e) {
			subjectDao.save(tmpSubjList);
			// TODO Inject some error message into form or similar
			throw new InvalidSubjectException("The same subject can't be added twice");
		}
		updateSubjectsForm.setId(user.getId());
		return updateSubjectsForm;
	}

}
