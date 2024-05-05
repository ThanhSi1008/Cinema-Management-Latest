package entity;

public class CustomersByHour {
	private int hour;
	private int numbersOfCustomer;

	public CustomersByHour(int hour, int numbersOfCustomer) {
		super();
		this.hour = hour;
		this.numbersOfCustomer = numbersOfCustomer;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getNumbersOfCustomer() {
		return numbersOfCustomer;
	}

	public void setNumbersOfCustomer(int numbersOfCustomer) {
		this.numbersOfCustomer = numbersOfCustomer;
	}

	@Override
	public String toString() {
		return "CustomersByHour [hour=" + hour + ", numbersOfCustomer=" + numbersOfCustomer + "]";
	}

}
