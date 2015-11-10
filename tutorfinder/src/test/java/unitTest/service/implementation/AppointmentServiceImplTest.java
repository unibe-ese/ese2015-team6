package unitTest.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
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
	
	private ArrayList<AppointmentPlaceholder> appointmentsList = new ArrayList<AppointmentPlaceholder>();
	private List<Timetable> timetableList = new ArrayList<Timetable>();
	private MakeAppointmentsForm makeAppointmentsForm = new MakeAppointmentsForm();
	
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
	
	@Test
	public void testLoadAppointments() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse("2015-11-07", formatter);
		
		List<AppointmentPlaceholder> gotList = appointmentService.loadAppointments(timetableList, mockTutor, date);
		
		assertEquals(Availability.AVAILABLE, gotList.get(0).getAvailability());
		assertEquals(DayOfWeek.SATURDAY, gotList.get(0).getDow());
	}

}
