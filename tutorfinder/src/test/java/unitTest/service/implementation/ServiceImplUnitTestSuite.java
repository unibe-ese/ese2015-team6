package unitTest.service.implementation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.unibe.ese.Tutorfinder.controller.service.implementations.BillServiceImpl;

@RunWith(Suite.class)
@SuiteClasses({
	RegisterServiceImplTest.class,
	ProfileServiceImplTest.class,
	UserServiceImplTest.class,
	TimetableServiceImplTest.class,
	FindTutorServiceImplTest.class,
	SubjectServiceImplTest.class,
	AppointmentServiceImplTest.class,
	PrepareFormServiceImplTest.class,
	BillServiceImplTest.class
})
/**
 * Test suite including all service implementation unit tests
 * 
 *
 */
public class ServiceImplUnitTestSuite {

}
