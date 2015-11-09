package integrationTest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration @ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml", 
		"file:src/main/webapp/WEB-INF/config/springData.xml" }) 

@Transactional 
@TransactionConfiguration(defaultRollback = true) 
public class HomeControllerTest { 
	
	@Autowired private WebApplicationContext wac; 

	private MockMvc mockMvc;


	@Before public void setup() {
	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void MappingTest() throws Exception{
		
		this.mockMvc.perform(
				post("/")).andExpect(model().hasNoErrors());
		
		
	}
	
	
	
}