package unitTest.service.implementation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	RegisterServiceImplTest.class,
	ProfileServiceImplTest.class,
	UserServiceImplTest.class,
	TimetableServiceImplTest.class,
	FindTutorServiceImplTest.class,
	SubjectServiceImplTest.class,
	AppointmentServiceImplTest.class,
	PrepareFormServiceImplTest.class
})
/**
 * Test suite including all service implementation unit tests
 * 
 *
 */
public class ServiceImplUnitTestSuite {

}
