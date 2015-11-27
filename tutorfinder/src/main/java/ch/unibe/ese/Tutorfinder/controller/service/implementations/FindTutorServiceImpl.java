package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;

@Service
public class FindTutorServiceImpl implements FindTutorService {
	
	@Autowired
	SubjectDao subjectDao;

	@Override
	public Map<User, List<Subject>> getSubjectsFrom(String query) {
		//TODO (maybe) implement search engine by hibernate
		LinkedList<Subject> subjectList = new LinkedList<Subject>();
		Iterable<Subject> subjectIterable = subjectDao.findAll();
		for(Subject subject: subjectIterable) {
			if (subject != null) {
				if(subject.getName().toLowerCase().contains(query.toLowerCase()))
					subjectList.add(subject);
			}
		}
		
		Map<User, List<Subject>> groupedMap = subjectList.stream()
				.collect(Collectors.groupingBy(Subject::getUser));
		
		return groupedMap;
	}

}
