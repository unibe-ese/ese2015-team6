package unitTest.service.implementation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.implementations.BillServiceImpl;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.BillDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.Availability;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/test.xml" })
public class BillServiceImplTest {
	
	
	private BillServiceImpl billServiceImpl;
	
	@Mock
	AppointmentService appointmentService ;
	
	@Mock
	BillDao billDao;
	
	@Mock
	UserDao userDao;
	
	@Mock
	Appointment appointment;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		billServiceImpl = new BillServiceImpl(appointmentService, billDao, userDao);
	}
	
	@Test
	public void getBillForCurentMonthTest()  {
		when(appointment.getWage()).thenReturn(BigDecimal.ZERO);
		List<Appointment> tmpList = new ArrayList<Appointment>();
		tmpList.add(appointment);
		
		when(appointmentService.getAppointmentsForMonthAndYear(any(User.class), 
				eq(Availability.ARRANGED), anyInt(), anyInt())).thenReturn(tmpList);
		
		BigDecimal testBalance = billServiceImpl.getBillForCurrentMonth(new User());
		
		assertTrue(BigDecimal.ZERO.compareTo(testBalance) <= 0);
	}
	
	@Test(expected=AssertionError.class)
	public void getBillForCurrentMonthNullTest() {
		billServiceImpl.getBillForCurrentMonth(null);
	}

}
