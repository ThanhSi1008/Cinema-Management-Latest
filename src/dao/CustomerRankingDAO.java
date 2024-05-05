package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.CustomerRanking;

public class CustomerRankingDAO {
	private ConnectDB connectDB;

	public CustomerRankingDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<CustomerRanking> getCustomerRankingByMonthAndYear(int month, int year) {
		Connection connection = null;
		List<CustomerRanking> customerRankingList = null;
		try {
			connection = connectDB.getConnection();
			PreparedStatement s = connection
					.prepareStatement("SELECT * FROM fn_CustomerSpendingStats(?, ?) order by totalspending desc");
			s.setInt(1, year);
			if (month == 0) {
				s.setNull(2, Types.INTEGER);
			} else {
				s.setInt(2, month);
			}
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
