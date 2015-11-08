package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.util.Availability;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	
	@Autowired
	AppointmentDao appointmentDao;

	public MakeAppointmentsForm saveFrom(MakeAppointmentsForm appForm, Integer slot, User tutor, User student) {
		BigDecimal wage = tutor.getProfile().getWage();
		LocalDate date = appForm.getDate();
		DayOfWeek dow = date.getDayOfWeek();
		LocalDateTime dateTime = LocalDateTime.from(date.atStartOfDay());
		dateTime = dateTime.plusHours(slot);
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		
		Appointment appointment = new Appointment(tutor,student,dow,timestamp,Availability.RESERVED, wage);
		appointmentDao.save(appointment);
		
		return appForm;
	}

	@Override
	public Appointment findByTutorAndTimestamp(User user, Timestamp timestamp) {
		assert (user != null && timestamp != null);
		
		Appointment returnValue = appointmentDao.findByTutorAndTimestamp(user, timestamp);

		return returnValue;
	}

}
