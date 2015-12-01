package unitTest.service.implementation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.SortOrder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.FindTutorFilterForm;
import ch.unibe.ese.Tutorfinder.controller.service.FindTutorService;
import ch.unibe.ese.Tutorfinder.controller.service.SubjectService;
import ch.unibe.ese.Tutorfinder.controller.service.implementations.FindTutorServiceImpl;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Subject;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.SubjectDao;
import ch.unibe.ese.Tutorfinder.util.SortCriteria;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class FindTutorServiceImplTest {

	@Autowired
	SubjectDao subjectDao;
	
	FindTutorService findTutorService;
	
	FindTutorFilterForm form;
	
	@Mock
	Comparator<User> mockComparator;
	@Mock
	Profile mockProfile;
	@Mock
	private Subject mockSubject;
	@Mock
	private User mockUser;
	@Mock
	private SubjectService mockSubjectService;
	
	private LinkedList<Subject> subjectList = new LinkedList<Subject>();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
		
		this.mockSubject = Mockito.mock(Subject.class);
		this.mockUser = Mockito.mock(User.class);
		
		ReflectionTestUtils.setField(mockSubject, "user", this.mockUser);
		
		subjectList.add(this.mockSubject);
		form = new FindTutorFilterForm();
		
		findTutorService = new FindTutorServiceImpl(mockSubjectService);
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
	
	@Test
	public void testGetSubjectsSorted() {
		when(subjectDao.findAll()).thenReturn(this.subjectList);
		when(mockSubject.getName()).thenReturn("TestSubject");
		when(mockSubject.getUser()).thenReturn(this.mockUser);
		when(subjectDao.findAll()).thenReturn(subjectList);
		
		findTutorService.setComparator(mockComparator);
		Map<User, List<Subject>> tmpSubjectList = findTutorService.getSubjectsSorted("TestSubject");
		
		assertTrue(tmpSubjectList.containsValue(this.subjectList));
	}
	
	@Test
	public void testSortingByRatingAsc() {
		form.setCriteria(SortCriteria.RATING);
		form.setOrder(SortOrder.ASCENDING);
		
		findTutorService.generateComparatorFrom(form);
		Comparator<User> testComparator = findTutorService.getComparator();
		when(mockUser.getProfile()).thenReturn(mockProfile);
		
		when(mockProfile.getRating()).thenReturn(null, BigDecimal.ONE);
		int testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(-1, testVal);
		
		when(mockProfile.getRating()).thenReturn(BigDecimal.ONE, null, BigDecimal.ZERO);
		testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(1, testVal);
		
		when(mockProfile.getRating()).thenReturn(BigDecimal.ZERO);
		when(mockProfile.getCountedRatings()).thenReturn(3l, 8l);
		testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(-1, testVal);
		
		when(mockProfile.getRating()).thenReturn(BigDecimal.ZERO);
		when(mockProfile.getCountedRatings()).thenReturn(4l, 1l);
		testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(1, testVal);
	}
	
	//@Test //FIXME won't be inverse on comparing...
	public void testSortingByRatingDesc() {
		form.setCriteria(SortCriteria.RATING);
		form.setOrder(SortOrder.DESCENDING);
	}
	
	@Test
	public void testSortingByGradeAsc() {
		form.setCriteria(SortCriteria.GRADE);
		form.setOrder(SortOrder.ASCENDING);
		
		findTutorService.generateComparatorFrom(form);
		Comparator<User> testComparator = findTutorService.getComparator();
		
		when(mockSubjectService.getAverageGradeByUser(mockUser)).thenReturn(3d, 5d);
		int testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(-1, testVal);
		
		when(mockSubjectService.getAverageGradeByUser(mockUser)).thenReturn(5.5d, 4d);
		testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(1, testVal);
		
		when(mockSubjectService.getAverageGradeByUser(mockUser)).thenReturn(5d, 5d);
		testVal = testComparator.compare(mockUser, mockUser);
		assertNotEquals(0, testVal);
	}
	
	@Test
	public void testFallback() {
		form.setCriteria(SortCriteria.RATING);
		form.setOrder(SortOrder.ASCENDING);
		
		findTutorService.generateComparatorFrom(form);
		Comparator<User> testComparator = findTutorService.getComparator();
		when(mockUser.getProfile()).thenReturn(mockProfile);
		
		when(mockProfile.getRating()).thenReturn(BigDecimal.ZERO);
		when(mockProfile.getCountedRatings()).thenReturn(0l);
		
		when(mockUser.getId()).thenReturn(5l,8l);
		int testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(-1, testVal);
		
		when(mockUser.getId()).thenReturn(8l,5l);
		testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(1, testVal);
		
		when(mockUser.getId()).thenReturn(1l,1l);
		testVal = testComparator.compare(mockUser, mockUser);
		assertEquals(0, testVal);
	}
	
	//@Test //TODO Can't mock equals so some cases can't be tested
	public void testFallbackException() {
		form.setCriteria(SortCriteria.RATING);
		form.setOrder(SortOrder.ASCENDING);
		
		findTutorService.generateComparatorFrom(form);
		@SuppressWarnings("unused")
		Comparator<User> testComparator = findTutorService.getComparator();
		when(mockUser.getProfile()).thenReturn(mockProfile);
		
		when(mockProfile.getRating()).thenReturn(BigDecimal.ZERO);
		when(mockProfile.getCountedRatings()).thenReturn(0l);
		
		when(mockUser.getId()).thenReturn(0l,0l);
	}
}
