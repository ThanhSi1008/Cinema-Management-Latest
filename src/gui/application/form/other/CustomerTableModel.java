package gui.application.form.other;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import dao.CustomerDAO;
import entity.Customer;

public class CustomerTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<Customer> customerList;
	private String[] columnNames = { "ID", "Full name", "Phone number", "Email", "Registration Date"};
	private CustomerDAO customerDAO;	

	public CustomerTableModel() {
		customerDAO = new CustomerDAO();
		customerList = customerDAO.getAllCustomer();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return customerList.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Customer customer = customerList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return customer.getCustomerID();
			case 1:
				return customer.getFullName();
			case 2:
				return customer.getPhoneNumber();
			case 3:
				return customer.getEmail();
			case 4:
				return customer.getRegDate();
		}
		return null;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;	
	}

}
