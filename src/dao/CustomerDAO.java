package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.Customer;

public class CustomerDAO {

	private ConnectDB connectDB;

	public CustomerDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<Customer> getAllCustomer() {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT CustomerID, FullName, PhoneNumber, Email, RegDate FROM Customer";
		List<Customer> customerList = new ArrayList<Customer>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String customerID = resultSet.getString(1);
				String fullName = resultSet.getString(2);
				String phoneNumber = resultSet.getString(3);
				String email = resultSet.getString(4);
				Date regDate = resultSet.getDate(5);
				customerList.add(new Customer(customerID, fullName, phoneNumber, email, regDate.toLocalDate()));
			}
			return customerList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String addNewCustomer(Customer customer) {
		Connection connection = connectDB.getConnection();
		String customerID = null;
		try {
			PreparedStatement s = connection.prepareStatement(
					"insert into customer (fullname, phonenumber, email, regdate) OUTPUT inserted.customerid values (?, ?, ?, ?)");
			s.setString(1, customer.getFullName());
			s.setString(2, customer.getPhoneNumber());
			s.setString(3, customer.getEmail());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			s.setString(4, customer.getRegDate().format(formatter));
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				customerID = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerID;
	}

	public String getCustomerIdByPhoneNumber(String phoneNumber) {
		Connection connection = connectDB.getConnection();
		String customerID = "";
		try {
			PreparedStatement s = connection.prepareStatement("select customerid from customer where phonenumber = ?");
			s.setString(1, phoneNumber);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				customerID = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerID;
	}

	public boolean checkDuplicatePhoneNumber(String phoneNumber) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement("select * from customer where phonenumber = ?");
			s.setString(1, phoneNumber);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Customer> findCustomerByName(String nameToFind) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT CustomerID, FullName, PhoneNumber, Email, RegDate FROM Customer WHERE FullName LIKE ?";
		List<Customer> customerList = new ArrayList<Customer>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, "%" + nameToFind + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String customerID = resultSet.getString(1);
				String fullName = resultSet.getString(2);
				String phoneNumber = resultSet.getString(3);
				String email = resultSet.getString(4);
				Date regDate = resultSet.getDate(5);
				customerList.add(new Customer(customerID, fullName, phoneNumber, email, regDate.toLocalDate()));
			}
			return customerList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Customer> findCustomerByPhoneNumber(String phoneNumberToFind) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT CustomerID, FullName, PhoneNumber, Email, RegDate FROM Customer WHERE PhoneNumber LIKE ?";
		List<Customer> customerList = new ArrayList<Customer>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, "%" + phoneNumberToFind + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String customerID = resultSet.getString(1);
				String fullName = resultSet.getString(2);
				String phoneNumber = resultSet.getString(3);
				String email = resultSet.getString(4);
				Date regDate = resultSet.getDate(5);
				customerList.add(new Customer(customerID, fullName, phoneNumber, email, regDate.toLocalDate()));
			}
			return customerList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean removeCustomerByID(String customerIDToDelete) {
		Connection connection = connectDB.getConnection();
		String deleteSQL = "DELETE FROM Customer WHERE CustomerID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, customerIDToDelete);
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0 ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Customer getCustomerByID(String customerIDToFind) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT CustomerID, FullName, PhoneNumber, Email, RegDate FROM Customer WHERE customerID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, customerIDToFind);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String customerID = resultSet.getString(1);
				String fullName = resultSet.getString(2);
				String phoneNumber = resultSet.getString(3);
				String email = resultSet.getString(4);
				Date regDate = resultSet.getDate(5);
				return new Customer(customerID, fullName, phoneNumber, email, regDate.toLocalDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateCustomer(Customer updatedCustomer) {
		Connection connection = connectDB.getConnection();
		String updateSQL = "UPDATE Customer SET FullName = ?, PhoneNumber = ?, Email = ?, RegDate = ? WHERE CustomerID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, updatedCustomer.getFullName());
			preparedStatement.setString(2, updatedCustomer.getPhoneNumber());
			preparedStatement.setString(3, updatedCustomer.getEmail());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			preparedStatement.setString(4, updatedCustomer.getRegDate().format(formatter));
			preparedStatement.setString(5, updatedCustomer.getCustomerID());
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0 ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Customer getCustomerByPhoneNumber(String phoneNumberToFind) {
		Connection connection = connectDB.getConnection();
		Customer customer = null;
		try {
			PreparedStatement s = connection.prepareStatement("select customerid, fullname, phonenumber, email, regdate from customer where phonenumber = ?");
			s.setString(1, phoneNumberToFind);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				String customerID = rs.getString(1);
				String fullName = rs.getString(2);
				String phoneNumber = rs.getString(3);
				String email = rs.getString(4);
				LocalDate regdate = rs.getDate(5).toLocalDate();
				return new Customer(customerID, fullName, phoneNumber, email, regdate);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

}
