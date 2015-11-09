package integrationTest.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	RegisterControllerTest.class,
	HomeControllerTest.class,
	ShowProfileControllerTest.class
})
/**
 * Test suite including all controller integration tests
 * 
 * @author Antonio
 *
 */
public class ControllerIntegrationTestSuite {

}
