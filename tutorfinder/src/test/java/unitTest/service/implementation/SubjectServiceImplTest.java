package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
	
	@Mock
	private User mockUser;
	@Mock
	private Principal mockAuthUser;
	@Mock
	private Subject mockSubject;
	@Mock
	private Row mockRow;
	private UpdateSubjectsForm updateSubjectsForm = new UpdateSubjectsForm();
	private ArrayList<Subject> subjectList = new ArrayList<Subject>();
	private ArrayList<Row> rows = new ArrayList<Row>();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
		
		this.subjectList.add(this.mockSubject);
		
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
		when(subjectDao.save(subjectList)).then(returnsFirstArg());
		
		//WHEN
		UpdateSubjectsForm tmpUpdateSubjectsForm = subjectService.saveFrom(this.updateSubjectsForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateSubjectsForm.getRows(), this.updateSubjectsForm.getRows());
		
	}
	
	@Test
	public void testSaveFromWhenNameNull() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(this.subjectList);
		when(mockRow.getSubject()).thenReturn(null);
		when(subjectDao.save(subjectList)).then(returnsFirstArg());
		
		//WHEN
		UpdateSubjectsForm tmpUpdateSubjectsForm = subjectService.saveFrom(this.updateSubjectsForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateSubjectsForm.getRows(), this.updateSubjectsForm.getRows());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=InvalidSubjectException.class)
	public void testSaveFromThrowsDataIntegrityViolationException() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(subjectDao.findAllByUser(any(User.class))).thenReturn(this.subjectList);
		when(mockRow.getSubject()).thenReturn("TestSubject");
		when(subjectDao.save(any(Iterable.class)))
		.thenThrow(new DataIntegrityViolationException("testException"))
		.thenReturn(subjectList);
		
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
	
	@Test
	public void testGetAverageGradeByUser() {
		this.subjectList.add(this.mockSubject);
		this.subjectList.add(this.mockSubject);
		this.subjectList.add(this.mockSubject);
		
		when(subjectDao.findAllByUser(mockUser)).thenReturn(subjectList);
		
		for (double var1 = 0; var1 <= 6.0; var1 = var1+0.5) {
			for (double var2 = 0; var2 <= 6.0; var2 = var2+0.5) {
				for (double var3 = 0; var3 <= 6.0; var3 = var3+0.5) {
					for (double var4 = 0; var4 <= 6.0; var4 = var4+0.5) {
						double testAverage = (var1 + var2 + var3 + var4)/4;
						when(mockSubject.getGrade()).thenReturn(var1, var2, var3, var4);
						double got = subjectService.getAverageGradeByUser(mockUser);
						assertEquals(testAverage, got, testAverage);
					}
				}
			}
		}
		
	}
	
}
