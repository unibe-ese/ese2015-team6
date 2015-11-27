package unitTest.service.implementation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class FindTutorServiceImplTest {

	@Autowired
	SubjectDao subjectDao;
	@Autowired
	FindTutorService findTutorService;
	
	private Subject mockSubject;
	private User mockUser;
	private LinkedList<Subject> subjectList = new LinkedList<Subject>();
	
	@Before
	public void setUp() {
		this.mockSubject = Mockito.mock(Subject.class);
		this.mockUser = Mockito.mock(User.class);
		
		ReflectionTestUtils.setField(mockSubject, "user", this.mockUser);
		
		subjectList.add(this.mockSubject);
		
	}
	
	@Test
	public void testGetSubjectsFrom() {
		//GIVEN
		when(subjectDao.findAll()).thenReturn(this.subjectList);
		when(mockSubject.getName()).thenReturn("TestSubject");
		when(mockSubject.getUser()).thenReturn(this.mockUser);
		
		//WHEN
		Map<User, List<Subject>> tmpSubjectList = findTutorService.getSubjectsFrom("TestSubject");
		
		//THEN
		assertTrue(tmpSubjectList.containsValue(this.subjectList));
	}
}
