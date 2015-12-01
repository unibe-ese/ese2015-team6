package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
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

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.ProfileService;
import ch.unibe.ese.Tutorfinder.controller.service.implementations.AppointmentServiceImpl;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Profile;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.util.Availability;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/test.xml"})
public class AppointmentServiceImplTest {
	
	@Autowired
	AppointmentDao appointmentDao;
	@Autowired
	AppointmentService appointmentService;
	
	@Mock
	private User mockTutor;
	@Mock
	private User mockStudent;
	@Mock
	private Profile mockProfile;
	@Mock
	private Appointment mockAppointment;
	@Mock
	private AppointmentPlaceholder mockAppointmentPlaceholder;
	@Mock
	private Timetable mockTimetable;
	@Mock
	private ProfileService mockProfileService;
	
	private ArrayList<Appointment> appList = new ArrayList<Appointment>();
	private ArrayList<AppointmentPlaceholder> appointmentsList = new ArrayList<AppointmentPlaceholder>();
	private List<Timetable> timetableList = new ArrayList<Timetable>();
	private MakeAppointmentsForm makeAppointmentsForm = new MakeAppointmentsForm();
	
	@After
	public void reset() {
		Mockito.reset(appointmentDao);
	}
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks( this );
		
		ReflectionTestUtils.setField(this.mockAppointmentPlaceholder, "availability", Availability.AVAILABLE);
		ReflectionTestUtils.setField(this.mockAppointmentPlaceholder, "dow", DayOfWeek.SATURDAY);
		ReflectionTestUtils.setField(this.mockAppointmentPlaceholder, "timeslot", 21);
		this.appointmentsList.add(this.mockAppointmentPlaceholder);
		
		ReflectionTestUtils.setField(this.mockTimetable, "user", this.mockTutor);
		ReflectionTestUtils.setField(this.mockTimetable, "day", DayOfWeek.SATURDAY);
		ReflectionTestUtils.setField(this.mockTimetable, "timeslot", 21);
		this.timetableList.add(this.mockTimetable);
		
		this.makeAppointmentsForm.setDate(LocalDate.of(2015, 11, 7));
		this.makeAppointmentsForm.setAppointments(this.appointmentsList);
		
		this.appList.add(this.mockAppointment);
	}
	
	@Test
	public void testSaveFrom() {
		//GIVEN
		int slot = 21;
		when(mockTutor.getProfile()).thenReturn(mockProfile);
		when(mockProfile.getWage()).thenReturn(BigDecimal.valueOf(15.50));
		
		//WHEN
		 MakeAppointmentsForm gotForm = appointmentService.saveFrom(makeAppointmentsForm, slot, mockTutor, mockStudent);
		
		//THEN
		assertEquals(makeAppointmentsForm, gotForm);
	}
		
	@Test
	public void testFindByTutorAndTimestamp() {
		//GIVEN
		Timestamp tmpTimestamp = Timestamp.valueOf("2015-11-07 21:00:00");
		when(appointmentDao.findByTutorAndTimestamp(mockTutor, tmpTimestamp)).thenReturn(this.mockAppointment);
		
		//WHEN
		Appointment tmpAppointment = appointmentService.findByTutorAndTimestamp(this.mockTutor, tmpTimestamp);
		
		//THEN
		assertEquals(tmpAppointment, this.mockAppointment);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testFindByNullTutorAndTimestamp() {
		//GIVEN
		Timestamp tmpTimestamp = Timestamp.valueOf("2015-11-07 21:00:00");
		
		//WHEN
		appointmentService.findByTutorAndTimestamp(null, tmpTimestamp);
		
	}
	
	@Test(expected=AssertionError.class)
	public void testFindByTutorAndNullTimestamp() {
		//WHEN
		appointmentService.findByTutorAndTimestamp(this.mockTutor, null);
		
	}
	
	@Test
	public void testFindByTutorAndDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse("2015-11-07", formatter);
		Timestamp tmpTimestamp = Timestamp.valueOf("2015-11-07 21:00:00");
		when(appointmentDao.findByTutorAndTimestamp(mockTutor, tmpTimestamp)).thenReturn(mockAppointment);
		when(mockAppointment.getAvailability()).thenReturn(Availability.AVAILABLE);
		
		AppointmentPlaceholder testAppointment = new AppointmentPlaceholder(DayOfWeek.SATURDAY, 21);
		
		List<AppointmentPlaceholder> gotList = appointmentService.findByTutorAndDate(mockTutor, date);
		
		assertEquals(testAppointment, gotList.get(0));
	}
	
	@Test(expected=AssertionError.class)
	public void testFindByNullTutorAndDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse("2015-11-07", formatter);
		
		appointmentService.findByTutorAndDate(null, date);
	}
	
	@Test(expected=AssertionError.class)
	public void testFindByTutorAndNullDate() {
		appointmentService.findByTutorAndDate(mockTutor, null);
	}
	
	@Test
	public void testLoadAppointments() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse("2015-11-07", formatter);
		
		List<AppointmentPlaceholder> gotList = appointmentService.loadAppointments(timetableList, mockTutor, date);
		
		assertEquals(Availability.AVAILABLE, gotList.get(0).getAvailability());
		assertEquals(DayOfWeek.SATURDAY, gotList.get(0).getDow());
	}
	
	@Test
	public void testLoadAppointmentsWithSaved() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse("2015-11-07", formatter);
		when(appointmentDao.findByTutorAndTimestamp(eq(mockTutor), any(Timestamp.class))).thenReturn(mockAppointment);
		when(mockAppointment.getAvailability()).thenReturn(Availability.AVAILABLE);
		
		AppointmentPlaceholder testApp = new AppointmentPlaceholder(date.getDayOfWeek(), 0);
		
		List<AppointmentPlaceholder> gotList = appointmentService.loadAppointments(timetableList, mockTutor, date);
		
		assertEquals(testApp, gotList.get(0));
	}
	
	@Test
	public void testGetPastAppointments() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpList = appointmentService.getPastAppointments(this.mockTutor, Availability.ARRANGED);
		
		assertEquals(this.appList, tmpList);
	}
	
	@Test
	public void testGetPastAppointmentsWhenEmpty() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(null);
		
		List<Appointment> tmpList = appointmentService.getPastAppointments(this.mockTutor, Availability.ARRANGED);
		
		
		assertTrue(tmpList.isEmpty());
	}
	
	@Test
	public void testGetPastAppointmentsWhenAppointmentIsNull() {
		this.appList.remove(this.mockAppointment);
		this.appList.add(null);
		
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpList = appointmentService.getPastAppointments(this.mockTutor, Availability.ARRANGED);
		
		assertTrue(tmpList.isEmpty());
	}
	
	@Test
	public void testGetPastAppointmentsWhenHaveFuture() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpList = appointmentService.getPastAppointments(this.mockTutor, Availability.ARRANGED);
		
		
		assertTrue(tmpList.isEmpty());
	}
	
	@Test(expected=AssertionError.class)
	public void testGetPastAppointmentsNullUser() {
		appointmentService.getPastAppointments(null, Availability.ARRANGED);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetPastAppointmentsNullAvailability() {
		appointmentService.getPastAppointments(this.mockTutor, null);
	}
	
	@Test
	public void testGetFutureAppointments() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpList = appointmentService.getFutureAppointments(this.mockTutor, Availability.ARRANGED);
		
		assertEquals(this.appList, tmpList);
	}
	
	@Test
	public void testGetFutureAppointmentsWhenEmpty() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(null);
		
		List<Appointment> tmpList = appointmentService.getFutureAppointments(this.mockTutor, Availability.ARRANGED);
		
		
		assertTrue(tmpList.isEmpty());
	}
	
	@Test
	public void testGetFutureAppointmentsWhenAppointmentIsNull() {
		this.appList.remove(this.mockAppointment);
		this.appList.add(null);
		
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpList = appointmentService.getFutureAppointments(this.mockTutor, Availability.ARRANGED);
		
		assertTrue(tmpList.isEmpty());
	}
	
	@Test
	public void testGetFutureAppointmentsWhenHaveFuture() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpList = appointmentService.getFutureAppointments(this.mockTutor, Availability.ARRANGED);
		
		
		assertTrue(tmpList.isEmpty());
	}
	
	@Test(expected=AssertionError.class)
	public void testgetFutureAppointmentsNullUser() {
		appointmentService.getFutureAppointments(null, Availability.ARRANGED);
	}
	
	@Test(expected=AssertionError.class)
	public void testGetFutureAppointmentsNullAvailability() {
		appointmentService.getFutureAppointments(this.mockTutor, null);
	}
	
	@Test
	public void testUdateAppointmentToReserved() {
		when(appointmentDao.findOne(anyLong())).thenReturn(this.mockAppointment);
		
		Appointment tmpAppointment = appointmentService.updateAppointment(Availability.RESERVED, new Long(1));
		
		assertEquals(this.mockAppointment, tmpAppointment);
	}
	
	@Test
	public void testUdateAppointmentToAvailability() {
		when(appointmentDao.findOne(anyLong())).thenReturn(this.mockAppointment);
		
		Appointment tmpAppointment = appointmentService.updateAppointment(Availability.AVAILABLE, new Long(1));
		
		assertNull(tmpAppointment);
	}
	
	@Test(expected=AssertionError.class)
	public void testUpdateAppointmentNullAvailability() {
		appointmentService.updateAppointment(null, new Long(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testUpdateAppointmentNullAppointmentId() {
		appointmentService.updateAppointment(Availability.AVAILABLE, null);
	}
	
	@Test
	public void testGetPendingAppointments() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.RESERVED)).thenReturn(this.appList);
		
		List<Appointment> tmpAppList = appointmentService.getPendingAppointments(this.mockStudent);
		
		assertEquals(appList, tmpAppList);
	}
	
	@Test
	public void testGetPendingAppointmentsWhenIsEmpty() {
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.RESERVED)).thenReturn(null);
		
		List<Appointment> tmpAppList = appointmentService.getPendingAppointments(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	public void testGetPendingAppointmentsWhenAppointmentIsNull() {
		ArrayList<Appointment> nullList = new ArrayList<Appointment>();
		nullList.add(null);
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.RESERVED)).thenReturn(nullList);
		
		List<Appointment> tmpAppList = appointmentService.getPendingAppointments(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	public void testGetPendingAppointmentsWhenNoFuture() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.RESERVED)).thenReturn(this.appList);
		
		List<Appointment> tmpAppList = appointmentService.getPendingAppointments(this.mockStudent);

		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test(expected=AssertionError.class)
	public void testGetPendingAppointmentsNullStudent() {
		appointmentService.getPendingAppointments(null);
	}
	
	@Test
	public void testGetFutureAppointmentAsStudent() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpAppList = appointmentService.getFutureAppointmentsAsStudent(this.mockStudent);
		
		assertEquals(appList, tmpAppList);
	}
	
	@Test
	public void testGetFutureAppointmentWhenIsEmptyAsStudent() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(null);
		
		List<Appointment> tmpAppList = appointmentService.getFutureAppointmentsAsStudent(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	public void testGetFutureAppointmentAsStudentWhenAppointmentIsNull() {
		ArrayList<Appointment> nullList = new ArrayList<Appointment>();
		nullList.add(null);
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(nullList);
		
		List<Appointment> tmpAppList = appointmentService.getFutureAppointmentsAsStudent(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	public void testGetFutureAppointmentAsStudentWhenNoFuture() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpAppList = appointmentService.getFutureAppointmentsAsStudent(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test(expected=AssertionError.class)
	public void testGetFutureAppointmentAsNullStudent() {
		appointmentService.getFutureAppointmentsAsStudent(null);
	}
	
	@Test
	public void testGetPastAppointmentAsStudent() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpAppList = appointmentService.getPastAppointmentsAsStudent(this.mockStudent);
		
		assertEquals(appList, tmpAppList);
	}
	
	@Test
	public void testGetPastAppointmentWhenIsEmptyAsStudent() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() - 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(null);
		
		List<Appointment> tmpAppList = appointmentService.getPastAppointmentsAsStudent(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	public void testGetPastAppointmentAsStudentWhenAppointmentIsNull() {
		ArrayList<Appointment> nullList = new ArrayList<Appointment>();
		nullList.add(null);
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(nullList);
		
		List<Appointment> tmpAppList = appointmentService.getPastAppointmentsAsStudent(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test
	public void testGetPastAppointmentAsStudentWhenNoPast() {
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp((new Date()).getTime() + 9999999));
		when(appointmentDao.findAllByStudentAndAvailabilityOrderByTimestampDesc(mockStudent, Availability.ARRANGED)).thenReturn(this.appList);
		
		List<Appointment> tmpAppList = appointmentService.getPastAppointmentsAsStudent(this.mockStudent);
		
		assertTrue(tmpAppList.isEmpty());
	}
	
	@Test(expected=AssertionError.class)
	public void testGetPastAppointmentAsNullStudent() {
		appointmentService.getPastAppointmentsAsStudent(null);
	}
	
	@Test
	public void testRatueTutorForAppointment() {
		this.appointmentService = new AppointmentServiceImpl(this.appointmentDao, this.mockProfileService);
		
		when(appointmentDao.findOne(anyLong())).thenReturn(this.mockAppointment);
		when(appointmentDao.findAllByTutor(mockTutor)).thenReturn(this.appList);
		when(mockAppointment.getRating()).thenReturn(BigDecimal.ONE);
		when(mockAppointment.getTutor()).thenReturn(this.mockTutor);
		when(mockTutor.getProfile()).thenReturn(mockProfile);
		
		appointmentService.rateTutorForAppointment(new Long(1), BigDecimal.ONE);
		
		verify(mockAppointment).setRating(BigDecimal.ONE);
		verify(appointmentDao).save(eq(mockAppointment));
		verify(mockProfileService).updateRating(eq(mockTutor), any(BigDecimal.class), any(BigDecimal.class));
	}
	
	@Test
	public void testRatueTutorForAppointmentWhenNoAppointmentsExist() {
		when(appointmentDao.findOne(anyLong())).thenReturn(this.mockAppointment);
		when(appointmentDao.save(mockAppointment)).thenReturn(this.mockAppointment);
		when(appointmentDao.findAllByTutor(mockTutor)).thenReturn(null);
		when(mockAppointment.getRating()).thenReturn(BigDecimal.ONE);
		when(mockAppointment.getTutor()).thenReturn(this.mockTutor);
		when(mockTutor.getProfile()).thenReturn(mockProfile);
		
		appointmentService.rateTutorForAppointment(new Long(1), BigDecimal.ONE);
		
		verify(mockAppointment).setRating(BigDecimal.ONE);
	}
	
	@Test
	public void testRatueTutorForAppointmentWhenAppointmentRatingIsNull() {
		when(appointmentDao.findOne(anyLong())).thenReturn(this.mockAppointment);
		when(appointmentDao.save(mockAppointment)).thenReturn(this.mockAppointment);
		when(appointmentDao.findAllByTutor(mockTutor)).thenReturn(this.appList);
		when(mockAppointment.getRating()).thenReturn(null);
		when(mockAppointment.getTutor()).thenReturn(this.mockTutor);
		when(mockTutor.getProfile()).thenReturn(mockProfile);
		
		appointmentService.rateTutorForAppointment(new Long(1), BigDecimal.ONE);
		
		verify(mockAppointment).setRating(BigDecimal.ONE);
	}           
	
	@Test(expected=AssertionError.class)
	public void testRatueTutorForAppointmentWhenIdIsNull() {
		appointmentService.rateTutorForAppointment(null, BigDecimal.ONE);
	}
	
	@Test(expected=AssertionError.class)
	public void testRatueTutorForAppointmentWhenRatingIsNull() {
		appointmentService.rateTutorForAppointment(new Long(1), null);
	}
	
	@Test 
	public void getAppointmentsForMonthAndYearTest() {
	//GIVEN
		LocalDate tmpDate = LocalDate.now();
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp(new Date().getTime()));
		     when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(appList);                                      
	//WHEN
		     List<Appointment> testList = appointmentService.getAppointmentsForMonthAndYear(mockTutor, Availability.ARRANGED, 
		    		 																			tmpDate.getMonthValue(), tmpDate.getYear());     
	//THEN
		     assertTrue(testList != null);
		     assertTrue(testList.size() == 1);
		     assertTrue(testList.get(0).equals(mockAppointment));
	}
	
	@Test 
	public void getAppointmentsForMonthAndYearTestDataBaseReturnsNull() {
	//GIVEN
		LocalDate tmpDate = LocalDate.now();
		when(mockAppointment.getTimestamp()).thenReturn(new Timestamp(new Date().getTime()));
		     when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(null);                                      
	//WHEN
		     List<Appointment> testList = appointmentService.getAppointmentsForMonthAndYear(mockTutor, Availability.ARRANGED, 
		    		 																			tmpDate.getMonthValue(), tmpDate.getYear());     
	//THEN
		     assertTrue(testList != null);
		     assertTrue(testList.size() == 0);
		    
	}
	
	public void getAppointmentsForMonthAndYearTestAppointmontOlderThanAMonth() {
		
		//GIVEN
				LocalDate tmpDate = LocalDate.now();
				when(mockAppointment.getTimestamp()).thenReturn(new Timestamp(0));
				     when(appointmentDao.findAllByTutorAndAvailabilityOrderByTimestampDesc(mockTutor, Availability.ARRANGED)).thenReturn(null);                                      
			//WHEN
				     List<Appointment> testList = appointmentService.getAppointmentsForMonthAndYear(mockTutor, Availability.ARRANGED, 
				    		 																			tmpDate.getMonthValue(), tmpDate.getYear());     
			//THEN
				     assertTrue(testList != null);
				     assertTrue(testList.size() == 0);
		
	}
	
	@Test(expected=AssertionError.class)
	public void getAppointmentsForMonthAndYearTestWhenTutorisNull() {
	  appointmentService.getAppointmentsForMonthAndYear(null, Availability.ARRANGED, 1, 2015);    
	}
	
	@Test(expected=AssertionError.class)
	public void getAppointmentsForMonthAndYearTestWhenAvailabilityisNull() {
	  appointmentService.getAppointmentsForMonthAndYear(mockTutor, null, 1, 2015);    
	}

}
