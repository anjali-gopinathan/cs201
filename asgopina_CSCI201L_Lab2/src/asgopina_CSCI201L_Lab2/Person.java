package asgopina_CSCI201L_Lab2;

public class Person {
	private String firstName;
	private String lastName;
	private String birthdate;
	
	public Person(String fn, String ln, String bd) {
		setFirstName(fn);
		setLastName(ln);
		setBirthdate(bd);
	}
	public void setFirstName(String fn) {
		firstName = fn;
	}
	public void setLastName(String ln) {
		lastName = ln;
	}
	public void setBirthdate(String bd) {
		birthdate = bd;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getBirthdate() {
		return birthdate;
	}
	
}
