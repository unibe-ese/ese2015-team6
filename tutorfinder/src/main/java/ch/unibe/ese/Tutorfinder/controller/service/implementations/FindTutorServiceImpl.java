package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.util.SortCriteria;

@Service
public class FindTutorServiceImpl implements FindTutorService {

	@Autowired
	SubjectDao subjectDao;
	
	@Autowired SubjectService subjectService;

	private Comparator<User> comparator;

	@Override
	public Map<User, List<Subject>> getSubjectsFrom(String query) {
		// TODO (maybe) implement search engine by hibernate
		LinkedList<Subject> subjectList = new LinkedList<Subject>();
		Iterable<Subject> subjectIterable = subjectDao.findAll();
		for (Subject subject : subjectIterable) {
			if (subject != null) {
				if (subject.getName().toLowerCase().contains(query.toLowerCase()))
					subjectList.add(subject);
			}
		}

		Map<User, List<Subject>> groupedMap = subjectList.stream().collect(Collectors.groupingBy(Subject::getUser));

		return groupedMap;
	}

	@Override
	public Map<User, List<Subject>> getSubjectsSorted(String query) {
		assert (comparator != null);
		Map<User, List<Subject>> queryResult = this.getSubjectsFrom(query);
		Map<User, List<Subject>> sortedResult = new TreeMap<User, List<Subject>>(comparator);
		sortedResult.putAll(queryResult);
		return sortedResult;
	}

	@Override
	public void generateComparatorFrom(FindTutorFilterForm form) {
		assert (SortCriteria.values().length == 3);
		switch (form.getCriteria()) {
		case RATING:
			comparator = new Comparator<User>() {

				@Override
				public int compare(User o1, User o2) {
					Profile profile1 = o1.getProfile();
					Profile profile2 = o2.getProfile();
					int comparedRating = profile1.getRating().compareTo(profile2.getRating());
					if (comparedRating == 0) {
						Long nr1 = new Long(profile1.getCountedRatings());
						Long nr2 = new Long(profile2.getCountedRatings());
						return nr1.compareTo(nr2);
					} else return comparedRating;
				}

			};
			break;
			
		case GRADE:
			comparator = new Comparator<User>() {

				@Override
				public int compare(User o1, User o2) {
					Double average1 =  subjectService.getAverageGradeByUser(o1);
					Double average2 =  subjectService.getAverageGradeByUser(o2);
					return average1.compareTo(average2);
				}
			};
			break;
			
		case ALPHABETICAL:
			comparator = new Comparator<User>() {

				@Override
				public int compare(User o1, User o2) {
					return o1.getLastName().compareToIgnoreCase(o2.getLastName());
				}
			};
		break;
		}

	}

	public Comparator<User> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<User> comparator) {
		this.comparator = comparator;
	}
}
