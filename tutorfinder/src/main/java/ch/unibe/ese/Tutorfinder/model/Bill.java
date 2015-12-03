package ch.unibe.ese.Tutorfinder.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ch.unibe.ese.Tutorfinder.util.PaymentStatus;

@Entity
public class Bill {

	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@ManyToOne
	private User tutor;

	@NotNull
	private String month;

	@NotNull
	private int monthValue;

	@NotNull
	private int year;

	@NotNull
	private BigDecimal amount;

	@NotNull
	private PaymentStatus paymentStatus;

	@NotNull
	private BigDecimal total;

	@NotNull
	private BigDecimal percentage;

	/* Constructor */
	public Bill() {
		super();
	}

	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public User getTutor() {
		return tutor;
	}

	public void setTutor(User tutor) {
		assert tutor != null : "Bills tutor is not allowed to be null!";
		this.tutor = tutor;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		assert month != null : "Bills month is not allowed to be null!";
		this.month = month;
	}

	public int getMonthValue() {
		return monthValue;
	}

	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		assert amount != null : "Bills amount is not allowed to be null!";
		this.amount = amount;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		assert total != null : "Bills total is not allowed to be null!";
		this.total = total;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		assert percentage != null : "Bills percentage is not allowed to be null!";
		this.percentage = percentage;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		assert paymentStatus != null : "Bills paymentStatus is not allowed to be null!";
		this.paymentStatus = paymentStatus;
	}

}
