package ch.unibe.ese.Tutorfinder.controller.service;

import java.math.BigDecimal;

import ch.unibe.ese.Tutorfinder.model.User;

public interface BillService {

	BigDecimal getBalance(User user);

}
