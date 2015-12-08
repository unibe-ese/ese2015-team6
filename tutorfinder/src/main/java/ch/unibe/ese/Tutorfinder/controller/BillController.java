package ch.unibe.ese.Tutorfinder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.Tutorfinder.controller.service.BillService;
import ch.unibe.ese.Tutorfinder.controller.service.UserService;
import ch.unibe.ese.Tutorfinder.model.User;
import ch.unibe.ese.Tutorfinder.util.PaymentStatus;

/**
 * Provides ModelAndView objects for the Spring MVC to load pages relevant to
 * the billing process.
 */
@Controller
public class BillController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BillService billService;

	@RequestMapping(value = "/bill", method=RequestMethod.GET)
	public ModelAndView bill(Principal authUser, @RequestParam(value = "payId", required = false) Long BillId) {
		ModelAndView model = new ModelAndView("bill");
		
		User tmpUser = userService.getUserByPrincipal(authUser);
		
		if(BillId != null) {
			billService.pay(tmpUser, BillId.longValue());
		}
		
		model.addObject("balance", billService.getBillForCurrentMonth(tmpUser));
		model.addObject("UnpaidBills", billService.getBills(tmpUser, PaymentStatus.UNPAID));
		model.addObject("PaidBills", billService.getBills(tmpUser, PaymentStatus.PAID));
		return model;
		
	}
}
