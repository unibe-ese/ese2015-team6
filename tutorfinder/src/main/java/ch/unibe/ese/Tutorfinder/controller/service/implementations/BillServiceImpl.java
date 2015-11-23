package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.BillService;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Bill;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.BillDao;
import ch.unibe.ese.Tutorfinder.model.dao.UserDao;
import ch.unibe.ese.Tutorfinder.util.Availability;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Service
public class BillServiceImpl implements BillService {
	
	@Autowired
	AppointmentService appointmentService;
	
	@Autowired
	BillDao billDao;
	
	@Autowired
	UserDao userDao;
	
	// Constructor for testing purposes
		@Autowired
		public BillServiceImpl(AppointmentService appointmentService, BillDao billDao,
								UserDao userDao){
			this.appointmentService = appointmentService;
			this.billDao = billDao;
			this.userDao = userDao;
		}

	@Override
	public BigDecimal getBillForCurrentMonth(User user) {
		
		assert(user != null);
		
		LocalDate tmpDate = LocalDate.now();
		List<Appointment> tmpList = appointmentService.getAppointmentsForMonthAndYear(user, Availability.ARRANGED,
										tmpDate.getMonthValue(), tmpDate.getYear());
		return totalWage(tmpList).multiply(ConstantVariables.PERCENTAGE);
	}

	/**
	 * takes a List of {@link Appointment}s and calculates the total wage earned
	 * @param appointments List of {@link Appointment}s, cannot be null
	 * @return sum of wages of the {@link Appointment}s in the given List
	 */
	private BigDecimal totalWage(List<Appointment> appointments) {
		assert(appointments != null);
		
		BigDecimal totalWage = BigDecimal.ZERO;
		
		for(Appointment appointment : appointments) {
			if(appointment != null) {
				totalWage = totalWage.add(appointment.getWage());
			}
		}	
		return totalWage;
	}

	@Override
	public Iterable<Bill> getBills(User user) {
		assert(user != null);
		
		return billDao.findAllByTutor(user);
	}

	@Override
	@Scheduled(cron="0 0 1 1 * ?") //execute method first of every month
	public void updateMonthlyBills() {
		
		Iterable<User> tutors = userDao.findAllByRole(ConstantVariables.TUTOR);
		
		for(User tutor : tutors) {
			createLastMonthsBill(tutor, LocalDate.now());
		}
		
	}

	/**
	 * calculates the previous month for a given Date and saves the bill for
	 * a given User for that month in the database
	 * @param tutor
	 * @param tmpDate
	 */
	private void createLastMonthsBill(User tutor, LocalDate tmpDate) {
		Bill tmpBill = new Bill();

		int monthValue = tmpDate.getMonthValue();
		int year = tmpDate.getYear();
		
		if(monthValue == 1) {
			monthValue = 12;
			year -= 1;
		} else {
			monthValue -= 1;
		}
		
		List<Appointment> tmpList = appointmentService.getAppointmentsForMonthAndYear(tutor, Availability.ARRANGED,
				monthValue, year);
		
		tmpBill.setMonthValue(monthValue);
		tmpBill.setMonth(getMonth(monthValue));
		tmpBill.setYear(year);
		tmpBill.setTutor(tutor);
		tmpBill.setAmount(totalWage(tmpList).multiply(ConstantVariables.PERCENTAGE));
		billDao.save(tmpBill);
		
		
	}
	
	/**
	 * takes the integer representation of a month and transforms it
	 * into a String containing the months name
	 * e.g 1 -> January
	 * 	   2 -> February
	 * ...
	 * @param monthValue integer representation of a month, must be between 1 and 12
	 * @return String with the months name corresponding to the given integer
	 */
	private String getMonth(int monthValue) {
		assert(monthValue >= 1);
		assert(monthValue <= 12);
	    return new DateFormatSymbols().getMonths()[monthValue-1];
	}

}
