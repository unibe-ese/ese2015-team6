package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.service.BillService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;

@Controller
public class BillController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	BillService billService;

	@RequestMapping(value = "/bill", method=RequestMethod.GET)
	public ModelAndView bill(Principal authUser) {
		ModelAndView model = new ModelAndView("bill");
		
		model.addObject("balance", billService.getBalance(userService.getUserByPrincipal(authUser)));
		
		return model;
		
	}
}
