package entity;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
	private String customerID;
	private String fullName;
	private String phoneNumber;
	private String email;
	private LocalDate regDate;

	public Customer() {
		super();
	}

	public Customer(String customerID, String fullName, String phoneNumber, String email, LocalDate regDate) {
		this.customerID = customerID;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.regDate = regDate;
	}

	public Customer(String phoneNumber, String fullName, String email, LocalDate now) {
		this.phoneNumber = phoneNumber;
		this.fullName = fullName;
		this.email = email;
		this.regDate = now;
	}

	public Customer(String customerID, String phoneNumber, String fullName, String email) {
		this.customerID = customerID;
		this.phoneNumber = phoneNumber;
		this.fullName = fullName;
		this.email = email;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", regDate=" + regDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(customerID, other.customerID);
	}

}
