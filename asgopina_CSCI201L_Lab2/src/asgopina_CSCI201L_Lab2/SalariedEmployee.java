package asgopina_CSCI201L_Lab2;

public class SalariedEmployee extends Employee {
	private double annualSalary;
	public SalariedEmployee(String firstName, String lastName, String birthdate, int employeeID, String jobTitle, String company, double annualSalary_) {
		super(firstName, lastName, birthdate, employeeID, jobTitle, company);
		setAnnualSalary(annualSalary_);
	}
	public double getAnnualSalary() {
		return annualSalary;
	}
	public void setAnnualSalary(double annualSalary) {
		this.annualSalary = annualSalary;
	}

}
