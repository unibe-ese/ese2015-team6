package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.exceptions.InvalidSubjectException;
import ch.unibe.ese.Tutorfinder.controller.pojos.Row;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateSubjectsForm;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class SubjectServiceImplTest {

	@Autowired
	SubjectDao subjectDao;
	@Autowired
	UserDao userDao;
	@Autowired 
	SubjectService subjectService;
	
	private User mockUser;
	private Principal mockAuthUser;
	private Subject mockSubject;
	private Row mockRow;
	private UpdateSubjectsForm updateSubjectsForm = new UpdateSubjectsForm();
	private ArrayList<Subject> subjectList = new ArrayList<Subject>();
	private ArrayList<Row> rows = new ArrayList<Row>();
	
	@Before
	public void setUp() {
		this.mockUser = Mockito.mock(User.class);
		this.mockAuthUser = Mockito.mock(Principal.class);
		
		this.mockSubject = Mockito.mock(Subject.class);
		this.subjectList.add(this.mockSubject);
		
		this.mockRow = Mockito.mock(Row.class);
		ReflectionTestUtils.setField(mockRow, "subject", "TestSubject");
		this.rows.add(mockRow);
		
		this.updateSubjectsForm.setRows(this.rows);
		
	}
	
	@Test
	public void testSaveFrom() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(this.subjectList);
		when(mockRow.getSubject()).thenReturn("TestSubject");
		
		//WHEN
		UpdateSubjectsForm tmpUpdateSubjectsForm = subjectService.saveFrom(this.updateSubjectsForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateSubjectsForm.getRows(), this.updateSubjectsForm.getRows());
		
	}
	
	//FIXME works when the (when...thenThrow) line is deleted
	@Test
	public void testSaveFromWhenNameNull() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(this.subjectList);
		when(mockRow.getSubject()).thenReturn(null);
		
		//WHEN
		UpdateSubjectsForm tmpUpdateSubjectsForm = subjectService.saveFrom(this.updateSubjectsForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateSubjectsForm.getRows(), this.updateSubjectsForm.getRows());
		
	}
	
	//FIXME DataIntegrityViolationException isn't catched
	@Test(expected=InvalidSubjectException.class)
	public void testSaveFromThrowsDataIntegrityViolationException() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(this.subjectList);
		when(mockRow.getSubject()).thenReturn("TestSubject");
		when(subjectDao.save(any(List.class))).thenThrow(new DataIntegrityViolationException("testException"));
		
		//WHEN
		UpdateSubjectsForm tmpUpdateSubjectsForm = subjectService.saveFrom(this.updateSubjectsForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateSubjectsForm.getRows(), this.updateSubjectsForm.getRows());
		
	}
	
	@Test
	public void testGetAllSubjectsByUser() {
		//GIVEN
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(this.subjectList);
		
		//WHEN
		ArrayList<Subject> tmpSubjectList = subjectService.getAllSubjectsByUser(this.mockUser);
		
		//THEN
		assertEquals(tmpSubjectList, this.subjectList);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetAllSubjectsByNullUser() {
		//WHEN
		subjectService.getAllSubjectsByUser(null);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testGetNullSubjectsByNullUser() {
		//GIVEN
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(null);
		
		//WHEN
		subjectService.getAllSubjectsByUser(this.mockUser);
		
	}
}
