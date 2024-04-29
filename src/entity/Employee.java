package entity;

import java.time.LocalDate;
import java.util.Arrays;

public class Employee {
	private String employeeID;
	private String fullName;
	private boolean gender;
	private LocalDate dateOfBirth;
	private String email;
	private String phoneNumber;
	private String role;
	private LocalDate startingDate;
	private double salary;
	private byte[] imageSource;

	public Employee() {
		super();
	}

	public Employee(String fullName, boolean gender, LocalDate dateOfBirth, String email, String phoneNumber,
			String role, LocalDate startingDate, double salary, byte[] imageSource) {
		super();
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.startingDate = startingDate;
		this.salary = salary;
		this.imageSource = imageSource;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public byte[] getImageSource() {
		return imageSource;
	}

	public void setImageSource(byte[] imageSource) {
		this.imageSource = imageSource;
	}

	@Override
	public String toString() {
		return "Employee [employeeID=" + employeeID + ", fullName=" + fullName + ", gender=" + gender + ", dateOfBirth="
				+ dateOfBirth + ", email=" + email + ", phoneNumber=" + phoneNumber + ", role=" + role
				+ ", startingDate=" + startingDate + ", salary=" + salary + ", imageSource="
				+ Arrays.toString(imageSource) + "]";
	}

}
