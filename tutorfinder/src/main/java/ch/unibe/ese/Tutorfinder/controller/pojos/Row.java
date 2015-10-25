package ch.unibe.ese.Tutorfinder.controller.pojos;

public class Row {
	private String subject;
	private double grade;
	
	public Row() {
		subject = "";
		grade = 0.0;
	}
	
	public Row(String subject, double grade){
		this.subject = subject;
		this.grade = grade;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	
}
