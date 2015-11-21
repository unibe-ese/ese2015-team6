package ch.unibe.ese.Tutorfinder.controller.service.implementations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.Tutorfinder.controller.pojos.AppointmentPlaceholder;
import ch.unibe.ese.Tutorfinder.controller.pojos.Forms.MakeAppointmentsForm;
import ch.unibe.ese.Tutorfinder.controller.service.AppointmentService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.Appointment;
import ch.unibe.ese.Tutorfinder.model.Timetable;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.model.dao.AppointmentDao;
import ch.unibe.ese.Tutorfinder.util.Availability;
import ch.unibe.ese.Tutorfinder.util.ConstantVariables;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	
	@Autowired
	AppointmentDao appointmentDao;
	@Autowired
	UserService userService;

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

	@Override
	public List<AppointmentPlaceholder> findByTutorAndDate(User user, LocalDate date) {
		assert(user != null && date != null);
		
		List<AppointmentPlaceholder> tmpList = new ArrayList<AppointmentPlaceholder>();
		
		for(int hours=0; hours <= ConstantVariables.TIMESLOTS; hours++) {
			
			LocalDateTime dateTime = LocalDateTime.from(date.atStartOfDay());
			dateTime = dateTime.plusHours(hours);
			Timestamp timestamp = Timestamp.valueOf(dateTime);
			
			Appointment tmpAppointment = appointmentDao.findByTutorAndTimestamp(user, timestamp);
			AppointmentPlaceholder placeholder;
			
			if(tmpAppointment != null) {
				placeholder = new AppointmentPlaceholder();
				placeholder.setAvailability(tmpAppointment.getAvailability());
				placeholder.setDow(date.getDayOfWeek());
				placeholder.setTimeslot(hours);
				tmpList.add(placeholder);
			}
		}
		
		return tmpList;
	}
	
	public List<AppointmentPlaceholder> loadAppointments(List<Timetable> slots, User tutor, LocalDate date) {
		
		List<AppointmentPlaceholder> tmpList = this.findByTutorAndDate(tutor, date);
		
		for (Timetable slot : slots) {
			int hours = slot.getTime();

			LocalDateTime dateTime = LocalDateTime.from(date.atStartOfDay());
			dateTime = dateTime.plusHours(hours);
			Timestamp timestamp = Timestamp.valueOf(dateTime);

			if (this.findByTutorAndTimestamp(tutor, timestamp) == null) {
				AppointmentPlaceholder placeholder = new AppointmentPlaceholder(date.getDayOfWeek(), hours);
				tmpList.add(placeholder);
			}
		}
		return tmpList;
	}
	
	@Override
	public List<Appointment> getPastAppointments(User tutor, Availability availability) {
		assert(tutor != null && availability != null);
		
		List<Appointment> appointments = appointmentDao.findAllByTutorAndAvailability(tutor, availability);
		
		Timestamp timestampNow = new Timestamp((new Date()).getTime());
		
		List<Appointment> pastAppointments = new ArrayList<Appointment>();
		
		if(appointments != null) {
			for(Appointment appointment : appointments) {
				if(appointment != null) {
					int compareResult = timestampNow.compareTo(appointment.getTimestamp());
					if(compareResult > 0)
						pastAppointments.add(appointment);
				}
			}
		}
		
		return pastAppointments;
	}
	
	@Override
	public List<Appointment> getFutureAppointments(User tutor, Availability availability) {
		assert(tutor != null && availability != null);
		
		List<Appointment> appointments = appointmentDao.findAllByTutorAndAvailability(tutor, availability);
		
		Timestamp timestampNow = new Timestamp((new Date()).getTime());
		
		List<Appointment> newAppointments = new ArrayList<Appointment>();
		
		if(appointments != null) {
			for(Appointment appointment : appointments) {
				if(appointment != null) {
					int compareResult = timestampNow.compareTo(appointment.getTimestamp());
					if(compareResult <= 0)
						newAppointments.add(appointment);
				}
			}
		}
		
		return newAppointments;
	}
	
	
	
	@Override
	public Appointment updateAppointment(Availability availability, Long appointmentId) {	
		assert(availability != null && appointmentId != null);
		if(availability.equals(Availability.AVAILABLE)) {
			appointmentDao.delete(appointmentId);
			return null;
		} else {
			Appointment tmpApp = appointmentDao.findOne(appointmentId);
			tmpApp.setAvailability(availability);
			
			appointmentDao.save(tmpApp);
			return appointmentDao.findOne(appointmentId);
		}
	}

	
	@Override
	public List<Appointment> getAppointmentsForMonthAndYear(User tutor, Availability availability, int month,
			int year) {
		assert(tutor != null && availability  != null);
		
		List<Appointment> appointments = appointmentDao.findAllByTutorAndAvailability(tutor, availability);
		List<Appointment> newAppointments = new ArrayList<Appointment>();
		
		LocalDate tmpDate;
		if(appointments != null) {
			for(Appointment appointment : appointments) {
				if(appointment != null) {
					tmpDate = appointment.getTimestamp().toLocalDateTime().toLocalDate();
					if(tmpDate.getMonthValue() == month && tmpDate.getYear() == year)
						newAppointments.add(appointment);
				}
			}
			
		}
		
		return newAppointments;
	}
	

	
	

}
