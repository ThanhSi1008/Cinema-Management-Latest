package entity;

public class CustomerRanking {

	private String customerID;
	private String customerName;
	private String phoneNumber;
	private double total;

	public CustomerRanking(String customerID, String customerName, String phoneNumber, double total) {
		super();
		this.customerID = customerID;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.total = total;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "CustomerRanking [customerID=" + customerID + ", customerName=" + customerName + ", phoneNumber="
				+ phoneNumber + ", total=" + total + "]";
	}

}
