package integrationTest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Bill;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.model.dao.BillDao;
import ch.unibe.ese.Tutorfinder.model.dao.ProfileDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.Availability;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;
import ch.unibe.ese.Tutorfinder.util.PaymentStatus;
import util.TestUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration 
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/testData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"}) 
@Transactional
public class BillControllerTest {
	
		@Autowired 
		private WebApplicationContext wac;
		
		@Autowired
		private UserDao userDao;
		@Autowired
		private ProfileDao profileDao;
		@Autowired
		private AppointmentDao appointmentDao;
		@Autowired
		private BillDao billDao;
	
		private MockMvc mockMvc;
		private Principal authUser;
	
		
		@Before 
		public void setUp() {
			this.mockMvc = MockMvcBuilders
					.webAppContextSetup(this.wac).build();
			TestUtility.initialize();
			TestUtility.setUp(userDao, profileDao);
			this.authUser = TestUtility.createPrincipal(TestUtility.testUser.getEmail(), "password", "ROLE_TUTOR");
		} 
		
		@Test
		@WithMockUser(roles="TUTOR")
		public void mappingBill() throws Exception {
			mockMvc.perform(get("/bill").principal(this.authUser))
					.andExpect(status().isOk());
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void noAppointmentBill() throws Exception {
			mockMvc.perform(get("/bill").principal(this.authUser))
							.andExpect(model().attribute("balance", BigDecimal.ZERO.setScale(2)));
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void noBillsTest() throws Exception {
			
			ArrayList<Bill> emptyList = new ArrayList<Bill>();
			
			mockMvc.perform(get("/bill").principal(this.authUser))
				.andExpect(model().attribute("UnpaidBills", emptyList))
				.andExpect(model().attribute("PaidBills", emptyList));
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void currentMonthBalanceOneAppointment() throws Exception {
			Appointment testAppointment = new Appointment();
			
			LocalDateTime dateTime = LocalDateTime.now();
			
			testAppointment.setAvailability(Availability.ARRANGED);
			testAppointment.setTutor(TestUtility.testUser);
			testAppointment.setStudent(TestUtility.testUserTwo);
			testAppointment.setWage(BigDecimal.TEN);
			testAppointment.setDay(dateTime.getDayOfWeek());
			testAppointment.setTimestamp(Timestamp.valueOf(dateTime));
			
			appointmentDao.save(testAppointment);
			
			mockMvc.perform(get("/bill").principal(this.authUser))
			.andExpect(model().attribute("balance", BigDecimal.TEN.multiply(ConstantVariables.PERCENTAGE).setScale(2)));	
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void currentMonthBalanceMultipleAppointments() throws Exception {
			Appointment testAppointment1 = new Appointment();
			Appointment testAppointment2 = new Appointment();
			
			LocalDateTime dateTime = LocalDateTime.now();
			
			testAppointment1.setAvailability(Availability.ARRANGED);
			testAppointment1.setTutor(TestUtility.testUser);
			testAppointment1.setStudent(TestUtility.testUserTwo);
			testAppointment1.setWage(BigDecimal.TEN);
			testAppointment1.setDay(dateTime.getDayOfWeek());
			testAppointment1.setTimestamp(Timestamp.valueOf(dateTime));
			
			appointmentDao.save(testAppointment1);
			
			
			testAppointment2.setAvailability(Availability.ARRANGED);
			testAppointment2.setTutor(TestUtility.testUser);
			testAppointment2.setStudent(TestUtility.testUserTwo);
			testAppointment2.setWage(BigDecimal.TEN);
			testAppointment2.setDay(dateTime.getDayOfWeek());
			testAppointment2.setTimestamp(Timestamp.valueOf(dateTime.minusHours(1)));
			
			appointmentDao.save(testAppointment2);
			
			BigDecimal reference = BigDecimal.TEN.multiply(ConstantVariables.PERCENTAGE);
			reference = reference.add(BigDecimal.TEN.multiply(ConstantVariables.PERCENTAGE));
			
			mockMvc.perform(get("/bill").principal(this.authUser))
			.andExpect(model().attribute("balance", reference.setScale(2)));
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void onePaidBill() throws Exception {
			Bill testBill = new Bill();
			
			testBill.setTutor(TestUtility.testUser);
			testBill.setPaymentStatus(PaymentStatus.PAID);
			testBill.setAmount(BigDecimal.TEN);
			testBill.setMonthValue(1);
			testBill.setMonth("January");
			testBill.setPercentage(ConstantVariables.PERCENTAGE);
			testBill.setTotal(BigDecimal.TEN);
			testBill.setYear(2015);
			
			testBill = billDao.save(testBill);
			
			ArrayList<Bill> testList = new ArrayList<Bill>();
			testList.add(testBill);
			
			mockMvc.perform(get("/bill").principal(this.authUser))
			.andExpect(model().attribute("PaidBills", testList));	
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void oneUnpaidBill() throws Exception {
			Bill testBill = new Bill();
			
			testBill.setTutor(TestUtility.testUser);
			testBill.setPaymentStatus(PaymentStatus.UNPAID);
			testBill.setAmount(BigDecimal.TEN);
			testBill.setMonthValue(1);
			testBill.setMonth("January");
			testBill.setPercentage(ConstantVariables.PERCENTAGE);
			testBill.setTotal(BigDecimal.TEN);
			testBill.setYear(2015);
			
			testBill = billDao.save(testBill);
			
			ArrayList<Bill> testList = new ArrayList<Bill>();
			testList.add(testBill);
			
			mockMvc.perform(get("/bill").principal(this.authUser))
			.andExpect(model().attribute("UnpaidBills", testList));
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void multipleUnpaidBills() throws Exception {
			Bill testBill1 = new Bill();
			Bill testBill2 = new Bill();
			
			testBill1.setTutor(TestUtility.testUser);
			testBill1.setPaymentStatus(PaymentStatus.UNPAID);
			testBill1.setAmount(BigDecimal.TEN);
			testBill1.setMonthValue(1);
			testBill1.setMonth("January");
			testBill1.setPercentage(ConstantVariables.PERCENTAGE);
			testBill1.setTotal(BigDecimal.TEN);
			testBill1.setYear(2015);
			
			testBill1 = billDao.save(testBill1);
			
			testBill2.setTutor(TestUtility.testUser);
			testBill2.setPaymentStatus(PaymentStatus.UNPAID);
			testBill2.setAmount(BigDecimal.TEN);
			testBill2.setMonthValue(2);
			testBill2.setMonth("February");
			testBill2.setPercentage(ConstantVariables.PERCENTAGE);
			testBill2.setTotal(BigDecimal.TEN);
			testBill2.setYear(2015);
			
			testBill2 = billDao.save(testBill2);
			
			ArrayList<Bill> testList = new ArrayList<Bill>();
			testList.add(testBill1);
			testList.add(testBill2);
			
			mockMvc.perform(get("/bill").principal(this.authUser))
			.andExpect(model().attribute("UnpaidBills", testList));
			
		}
		
		@Test
		@WithMockUser(roles="Tutor")
		public void multiplePaidBills() throws Exception {
			Bill testBill1 = new Bill();
			Bill testBill2 = new Bill();
			
			testBill1.setTutor(TestUtility.testUser);
			testBill1.setPaymentStatus(PaymentStatus.PAID);
			testBill1.setAmount(BigDecimal.TEN);
			testBill1.setMonthValue(1);
			testBill1.setMonth("January");
			testBill1.setPercentage(ConstantVariables.PERCENTAGE);
			testBill1.setTotal(BigDecimal.TEN);
			testBill1.setYear(2015);
			
			testBill1 = billDao.save(testBill1);
			
			testBill2.setTutor(TestUtility.testUser);
			testBill2.setPaymentStatus(PaymentStatus.PAID);
			testBill2.setAmount(BigDecimal.TEN);
			testBill2.setMonthValue(2);
			testBill2.setMonth("February");
			testBill2.setPercentage(ConstantVariables.PERCENTAGE);
			testBill2.setTotal(BigDecimal.TEN);
			testBill2.setYear(2015);
			
			testBill2 = billDao.save(testBill2);
			
			ArrayList<Bill> testList = new ArrayList<Bill>();
			testList.add(testBill1);
			testList.add(testBill2);
			
			mockMvc.perform(get("/bill").principal(this.authUser))
			.andExpect(model().attribute("PaidBills", testList));
			
		}	
}
