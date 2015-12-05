package integrationTest.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	LoginControllerTest.class,
	MessageControllerTest.class,
	BillControllerTest.class,
	TimetableControllerTest.class,
	SubjectControllerTest.class
})
/**
 * Test suite including all controller integration tests
 * 
 * @author Antonio
 *
 */
public class ControllerIntegrationTestSuite {

}
