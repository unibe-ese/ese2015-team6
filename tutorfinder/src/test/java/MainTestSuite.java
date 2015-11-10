import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import integrationTest.controller.ControllerIntegrationTestSuite;
import unitTest.service.implementation.ServiceImplUnitTestSuite;

@RunWith(Suite.class)
@SuiteClasses({
	ServiceImplUnitTestSuite.class,
	ControllerIntegrationTestSuite.class
	
})
/**
 * Test suite including all tests
 * 
 * @author Antonio
 *
 */
public class MainTestSuite {

}
