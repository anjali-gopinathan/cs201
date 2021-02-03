package asgopina_CSCI201L_Lab2;

public class CommissionEmployee extends SalariedEmployee {
	private int salesTotal;
	private double commissionPercentage;
	
	public CommissionEmployee (String firstName, String lastName, String birthdate, int employeeID, String jobTitle, String company, double baseSalary_, int salesTotal_, double commissionPercentage_) {
		super(firstName, lastName, birthdate, employeeID, jobTitle, company, baseSalary_);
		setSalesTotal(salesTotal_);
		setCommissionPercentage(commissionPercentage_);
	}
	public double getAnnualSalary() {
		return super.getAnnualSalary() + (commissionPercentage * salesTotal);
	}
	public double getSalesTotal() {
		return salesTotal;
	}

	public void setSalesTotal(int salesTotal) {
		this.salesTotal = salesTotal;
	}

	public double getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(double commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}
	
}
