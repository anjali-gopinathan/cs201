package asgopina_CSCI201L_Lab2;

public abstract class Employee extends Person{
	private int employeeID;
	private String jobTitle;
	private String company;

	public Employee(String firstName, String lastName, String birthdate, int employeeID, String jobTitle, String company) {
		super(firstName,  lastName, birthdate);
		setEmployeeID(employeeID);
		setJobTitle(jobTitle);
		setCompany(company);
	}
	public abstract double getAnnualSalary();
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public String getCompany() {
		return company;
	}
}
