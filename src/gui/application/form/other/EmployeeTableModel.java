package gui.application.form.other;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import dao.EmployeeDAO;
import entity.Customer;
import entity.Employee;

public class EmployeeTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<Employee> employeeList;
	private String[] columnNames = { "ID", "Full name", "Gender", "Phone Number", "Email", "Role"};
	private EmployeeDAO employeeDAO;	

	public EmployeeTableModel() {
		employeeDAO = new EmployeeDAO();
		employeeList = employeeDAO.getAllEmployee();
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
		case 5:
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
		return employeeList.size();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Employee employee = employeeList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return employee.getEmployeeID();
			case 1:
				return employee.getFullName();
			case 2:
				return employee.isGender() ? "Male" : "Female";
			case 3:
				return employee.getPhoneNumber();
			case 4:
				return employee.getEmail();
			case 5:
				return employee.getRole();
		}
		return null;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;	
	}

}
