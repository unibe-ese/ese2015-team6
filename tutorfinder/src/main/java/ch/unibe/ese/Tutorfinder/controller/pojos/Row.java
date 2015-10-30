package ch.unibe.ese.Tutorfinder.controller.pojos;

/**
 * Is used to make up an {@link UpdateSubjectsForm} and contains it's subject 
 * and respective grading values
 * 
 * @author Antonio, Florian, Nicola, Lukas
 *
 */
public class Row {
	
	private String subject;
	
	private double grade;
	
	
	/* Constructors */
	public Row() {
		subject = "";
		grade = 0.0;
	}
	
	public Row(String subject, double grade){
		this.subject = subject;
		this.grade = grade;
	}

	
	/* Getters and Setters */
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
