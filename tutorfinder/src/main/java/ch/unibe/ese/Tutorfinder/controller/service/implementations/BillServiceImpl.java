package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.BillService;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.Availability;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Service
public class BillServiceImpl implements BillService {
	
	@Autowired
	AppointmentService appointmentService;

	@Override
	public BigDecimal getBalance(User user) {
		
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

}
