import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import integrationTest.controller.ControllerIntegrationTestSuite;
import unitTest.controller.ControllerUnitTestSuite;
import unitTest.service.implementation.ServiceImplUnitTestSuite;

@RunWith(Suite.class)
@SuiteClasses({
	ServiceImplUnitTestSuite.class,
	ControllerIntegrationTestSuite.class,
	ControllerUnitTestSuite.class
})
/**
 * Test suite including all tests
 * 
 *
 */
public class MainTestSuite {

}
