import java.util.Scanner;

public class Salary {

	public static void main(String [] args) {
		System.out.print("How many employees? ");
		Scanner scan = new Scanner(System.in);
		int numEmployees = scan.nextInt();
		Employee empArr[] = new Employee[numEmployees];
		
		for (int i=0; i < numEmployees; i++) {
			System.out.print("Please enter the name: ");
			String name = scan.next();
			System.out.print("Please enter the hourly rate: ");
			float rate = scan.nextFloat();
			empArr[i] = new Employee();
			empArr[i].setName(name);
			empArr[i].setRate(rate);
		}
		for (int i=0; i < numEmployees; i++) {
			System.out.println(empArr[i].getName() + "'s annual salary is " + empArr[i].getRate() * 40 * 52);
		}
		scan.close();
	}
}

class Employee {	// fully encapsulated
	private float rate;
	private String name;
	
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}