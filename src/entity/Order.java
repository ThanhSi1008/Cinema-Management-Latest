package entity;

import java.time.LocalDateTime;

public class Order {
	private String orderID;
	private LocalDateTime orderDate;
	private int quantityTicket;
	private String note;
	private double total;
	private Customer customer;
	private Employee employee;
	private MovieSchedule schedule;

	public Order(int quantityTicket, String note, Customer customer, Employee employee, MovieSchedule schedule) {
		super();
		this.orderDate = LocalDateTime.now();
		this.quantityTicket = quantityTicket;
		this.note = note;
		setTotal();
		this.customer = customer;
		this.employee = employee;
		this.schedule = schedule;
	}

	public Order(LocalDateTime orderDate, int quantityTicket, String note, double total, Customer customer,
			Employee employee, MovieSchedule schedule) {
		super();
		this.orderDate = orderDate;
		this.quantityTicket = quantityTicket;
		this.note = note;
		this.total = total;
		this.customer = customer;
		this.employee = employee;
		this.schedule = schedule;
	}

	public Order(String orderID, LocalDateTime orderDate, int quantityTicket, String note, double total,
			Customer customer, Employee employee, MovieSchedule schedule) {
		super();
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.quantityTicket = quantityTicket;
		this.note = note;
		this.total = total;
		this.customer = customer;
		this.employee = employee;
		this.schedule = schedule;
	}

	public String getOrderID() {
		return orderID;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderDate() {
		this.orderDate = LocalDateTime.now();
	}

	public int getQuantityTicket() {
		return quantityTicket;
	}

	public void setQuantityTicket(int quantityTicket) {
		this.quantityTicket = quantityTicket;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal() {
		// viet phuong thuc tinh tong
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setOrderID(String orderID) {
		if (this.orderID == null) {
			this.orderID = orderID;
		}
	}

	public MovieSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(MovieSchedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", orderDate=" + orderDate + ", quantityTicket=" + quantityTicket
				+ ", note=" + note + ", total=" + total + ", customer=" + customer + ", employee=" + employee
				+ ", schedule=" + schedule + "]";
	}

}
