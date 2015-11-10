package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorForm;
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
	private FindTutorForm findTutorForm = new FindTutorForm();
	private LinkedList<Subject> subjectList = new LinkedList<Subject>();
	
	@Before
	public void setUp() {
		this.mockSubject = Mockito.mock(Subject.class);
		this.mockUser = Mockito.mock(User.class);
		
		ReflectionTestUtils.setField(mockSubject, "user", this.mockUser);
		subjectList.add(this.mockSubject);
		
	}
	
	@Test
	public void testGetUsersFrom() {
		//GIVEN
		LinkedList<User> tmpMockUser = new LinkedList<User>();
		tmpMockUser.add(this.mockUser);
		when(subjectDao.findByName(anyString())).thenReturn(this.subjectList);
		when(mockSubject.getUser()).thenReturn(this.mockUser);

		//WHEN
		Iterable<User> tmpUser = findTutorService.getUsersFrom(this.findTutorForm);
		
		//THEN
		assertEquals(tmpUser, tmpMockUser);
	}
	
	@Test
	public void testGetNullUsersFrom() {
		//GIVEN
		when(subjectDao.findByName(anyString())).thenReturn(this.subjectList);
		when(mockSubject.getUser()).thenReturn(null);

		//WHEN
		Iterable<User> tmpUser = findTutorService.getUsersFrom(this.findTutorForm);
		
		//THEN
		assertEquals(tmpUser, new LinkedList<User>());
	}
	
	@Test
	public void testGetSubjectsFrom() {
		//GIVEN
		when(subjectDao.findByName(anyString())).thenReturn(this.subjectList);
		
		//WHEN
		LinkedList<Subject> tmpSubjectList = findTutorService.getSubjectsFrom("TestSubject");
		
		//THEN
		assertEquals(tmpSubjectList, this.subjectList);
	}
}
