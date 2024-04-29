package entity;

import java.time.LocalDate;

public class Customer {
	private String customerID;
	private String fullName;
	private String gender;
	private LocalDate dateOfBirth;
	private String phoneNumber;
	private String email;
	private LocalDate regDate;

	public Customer(String fullName, String gender, LocalDate dateOfBirth, String phoneNumber, String email) {
		super();
		this.fullName = fullName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.email = email;
		setRegDate();
	}

	public Customer() {
		super();
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate() {
		this.regDate = LocalDate.now();
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", fullName=" + fullName + ", gender=" + gender + ", dateOfBirth="
				+ dateOfBirth + ", phoneNumber=" + phoneNumber + ", email=" + email + ", regDate=" + regDate + "]";
	}

}
