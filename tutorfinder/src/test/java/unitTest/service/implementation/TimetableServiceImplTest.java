package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.UpdateTimetableForm;
import ch.unibe.ese.Tutorfinder.controller.service.TimetableService;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.TimetableDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class TimetableServiceImplTest {
	
	@Autowired
	UserDao userDao;
	@Autowired
	TimetableDao timetableDao;
	@Autowired
	TimetableService timetableService;
	
	@Mock
	private Timetable mockTimetable;
	@Mock
	private User mockUser;
	@Mock
	private Principal mockAuthUser;
	private UpdateTimetableForm updateTimetableForm = new UpdateTimetableForm();
	private Boolean[][] timetable = new Boolean[ConstantVariables.TIMESLOTS][ConstantVariables.DAYS];
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
		
		this.updateTimetableForm.setId(new Long(1));
		for(Boolean[] row : this.timetable) {
			Arrays.fill(row, false);
		}
		this.timetable[1][1] = true;
		this.updateTimetableForm.setTimetable(this.timetable);
	}
	
	@Test
	public void testSaveFrom() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(timetableDao.findByUserAndDayAndTimeslot(any(User.class), any(DayOfWeek.class), anyInt())).thenReturn(this.mockTimetable);
		when(timetableDao.save(any(Timetable.class))).thenReturn(this.mockTimetable);
		
		//WHEN
		UpdateTimetableForm tmpUpdateTimetableForm = timetableService.saveFrom(this.updateTimetableForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateTimetableForm, this.updateTimetableForm);
		
	}
	
	@Test
	public void testSaveFromWithNewTimetable() {
		//GIVEN
		when(userDao.findByEmail(anyString())).thenReturn(this.mockUser);
		when(timetableDao.findByUserAndDayAndTimeslot(any(User.class), any(DayOfWeek.class), anyInt())).thenReturn(null);
		when(timetableDao.save(any(Timetable.class))).thenReturn(this.mockTimetable);
		
		//WHEN
		UpdateTimetableForm tmpUpdateTimetableForm = timetableService.saveFrom(this.updateTimetableForm, this.mockAuthUser);
		
		//THEN
		assertEquals(tmpUpdateTimetableForm, this.updateTimetableForm);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testSaveFromWithNullForm() {		
		//WHEN
		timetableService.saveFrom(null, this.mockAuthUser);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testSaveFromWithNullUser() {		
		//WHEN
		timetableService.saveFrom(this.updateTimetableForm, null);
		
	}
	
	@Test
	public void testFindAllByUser() {
		//GIVEN
		ArrayList<Timetable> timetableList = new ArrayList<Timetable>();
		timetableList.add(this.mockTimetable);
		when(timetableDao.findAllByUser(any(User.class))).thenReturn(timetableList);
		
		//WHEN
		List<Timetable> tmpTimetableList = timetableService.findAllByUser(this.mockUser);
		
		//THEN
		assertTrue(tmpTimetableList.contains(this.mockTimetable));
		
	}
	
	@Test(expected=AssertionError.class)
	public void testFindAllByUserNull() {
		//WHEN
		timetableService.findAllByUser(null);

	}
	
	@Test(expected=AssertionError.class)
	public void testFindNullByUser() {
		//GIVEN
		when(timetableDao.findAllByUser(any(User.class))).thenReturn(null);
		
		//WHEN
		timetableService.findAllByUser(this.mockUser);

	}
	
	@Test
	public void testFindAllByUserAndDay() {
		//GIVEN
		ArrayList<Timetable> timetableList = new ArrayList<Timetable>();
		timetableList.add(this.mockTimetable);
		when(timetableDao.findAllByUserAndDay(any(User.class), any(DayOfWeek.class))).thenReturn(timetableList);
		
		//WHEN
		List<Timetable> tmpTimetableList = timetableService.findAllByUserAndDay(this.mockUser, DayOfWeek.MONDAY);
		
		//THEN
		assertTrue(tmpTimetableList.contains(this.mockTimetable));
		
	}
	
	@Test(expected=AssertionError.class)
	public void testFindAllByUserAndDayNull() {
		//WHEN
		timetableService.findAllByUserAndDay(this.mockUser, null);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testFindAllByUserNullAndDay() {
		//WHEN
		timetableService.findAllByUserAndDay(null, DayOfWeek.MONDAY);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testFindNullByUserAndDay() {
		//GIVEN
		when(timetableDao.findAllByUserAndDay(any(User.class), any(DayOfWeek.class))).thenReturn(null);
		
		//WHEN
		timetableService.findAllByUserAndDay(this.mockUser, DayOfWeek.MONDAY);
	
	}

}
