package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.SortOrder;

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
	
	@Autowired
	public FindTutorServiceImpl(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

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
		assert comparator != null:"Comparator was not initialized";
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
					BigDecimal rating1 = profile1.getRating();
					if (rating1 == null) rating1 = BigDecimal.ZERO;
					BigDecimal rating2 = profile2.getRating();
					if (rating2 == null) rating2 = BigDecimal.ZERO;
					int compared = rating1.compareTo(rating2);
					if (compared == 0) {
						Long nr1 = new Long(profile1.getCountedRatings());
						Long nr2 = new Long(profile2.getCountedRatings());
						compared = nr1.compareTo(nr2);
						if (compared == 0) compared = fallback(o1,o2);
					}
					return compared;
				}

			};
			if (form.getOrder().equals(SortOrder.DESCENDING)) comparator = comparator.reversed();
			break;
			
		case GRADE:
			comparator = new Comparator<User>() {

				@Override
				public int compare(User o1, User o2) {
					Double average1 =  subjectService.getAverageGradeByUser(o1);
					Double average2 =  subjectService.getAverageGradeByUser(o2);
					
					int compared = average1.compareTo(average2);
					if (compared == 0) compared = fallback(o1,o2);
					return compared;
				}
			};
			if (form.getOrder().equals(SortOrder.DESCENDING)) comparator = comparator.reversed();
			break;
			
		case ALPHABETICAL:
			comparator = new Comparator<User>() {

				@Override
				public int compare(User o1, User o2) {
					int compared = o1.getLastName().compareToIgnoreCase(o2.getLastName());
					if (compared == 0) compared = fallback(o1,o2);
					return compared;
				}
			};
			if (form.getOrder().equals(SortOrder.DESCENDING)) comparator = comparator.reversed();
		break;
		}

	}

	/**
	 * Fallback method for comparators
	 * @param o2 
	 * @param o1 
	 * @return compareTo of two users IDs
	 */
	protected int fallback(User o1, User o2) {
		Long id1 = o1.getId();
		Long id2 = o2.getId();
		int compared = id1.compareTo(id2);
		assert compared != 0 || o1.equals(o2) : "User id is supposed to be unique!";
		return compared;
	}

	@Override
	public Comparator<User> getComparator() {
		return comparator;
	}

	@Override
	public void setComparator(Comparator<User> comparator) {
		this.comparator = comparator;
	}
}
