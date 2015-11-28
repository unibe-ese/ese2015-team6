package unitTest.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ShowProfileControllerUnitTests.class,
	LoginControllerUnitTests.class,
	AppointmentsOverviewUnitTests.class,
	UpdateProfileControllerUnitTests.class,
	MessagesControllerUnitTests.class
})
/**
 * Test suite including all controller unit tests
 *
 */
public class ControllerUnitTestSuite {

}
