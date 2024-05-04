package gui.application.form.other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;

public class CustomerRankingDAO {
	private ConnectDB connectDB;

	public CustomerRankingDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<CustomerRanking> getCustomerRankingByMonthAndYear(int month, int year) {
		Connection connection = connectDB.getConnection();
		List<CustomerRanking> customerRankingList = null;
		try {
			PreparedStatement s = connection.prepareStatement("SELECT * FROM fn_CustomerSpendingStats(?, ?)");
			s.setInt(1, year);
			s.setInt(2, month);
			customerRankingList = new ArrayList<CustomerRanking>();
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String customerID = rs.getString(1);
				String customerName = rs.getString(2);
				String phoneNumber = rs.getString(3);
				double totalSpending = rs.getDouble(4);
				customerRankingList.add(new CustomerRanking(customerID, customerName, phoneNumber, totalSpending)); 
			}
			return customerRankingList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerRankingList;
	}
	
	
}
