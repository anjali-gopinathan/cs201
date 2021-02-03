package asgopina_CSCI201L_Lab2;

public class HourlyEmployee extends Employee {
	private double hourlyRate;
	private int numberHoursPerWeek;
	
	public HourlyEmployee (String firstName, String lastName, String birthdate, int employeeID, String jobTitle, String company, double hourlyRate_, int numberHoursPerWeek_) {
		super(firstName, lastName, birthdate, employeeID, jobTitle, company);
		setHourlyRate(hourlyRate_);
		setNumberHoursPerWeek(numberHoursPerWeek_);
	}
	public double getAnnualSalary() {
		return hourlyRate * numberHoursPerWeek * 52;
	}
	public double getHourlyRate() {
		return hourlyRate;
	}
	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	public int getNumberHoursPerWeek() {
		return numberHoursPerWeek;
	}
	public void setNumberHoursPerWeek(int numberHoursPerWeek) {
		this.numberHoursPerWeek = numberHoursPerWeek;
	}
}
