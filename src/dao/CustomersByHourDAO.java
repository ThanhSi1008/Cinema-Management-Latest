package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.CustomersByHour;

public class CustomersByHourDAO {
	private ConnectDB connectDB;

	public CustomersByHourDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<CustomersByHour> getCustomerCountPerHour(int year, int month, int day) {
		List<CustomersByHour> customerCountPerHour = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		String querySQL = "SELECT * FROM fn_CountCustomersPerHour(?, ?, ?)";

		try {
			customerCountPerHour = new ArrayList<CustomersByHour>();
			connection = connectDB.getConnection();
			s = connection.prepareStatement(querySQL);

			s.setInt(1, year);
			s.setInt(2, month);
			s.setInt(3, day);

			rs = s.executeQuery();

			while (rs.next()) {
				int hour = rs.getInt(1);
				int numberCustomer = rs.getInt(2);

				customerCountPerHour.add(new CustomersByHour(hour, numberCustomer));
			}
			return customerCountPerHour;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return customerCountPerHour;
	}
		
	public List<CustomersByHour> GetSeatSoldByHour(int year, int month, int day) {
		List<CustomersByHour> customerCountPerHour = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		String querySQL = "SELECT * FROM GetSeatSoldByHour(?, ?, ?)";

		try {
			customerCountPerHour = new ArrayList<CustomersByHour>();
			connection = connectDB.getConnection();
			s = connection.prepareStatement(querySQL);

			s.setInt(1, year);
			s.setInt(2, month);
			s.setInt(3, day);

			rs = s.executeQuery();

			while (rs.next()) {
				int hour = rs.getInt(1);
				int numberCustomer = rs.getInt(2);

				customerCountPerHour.add(new CustomersByHour(hour, numberCustomer));
			}
			return customerCountPerHour;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return customerCountPerHour;
	}

}
