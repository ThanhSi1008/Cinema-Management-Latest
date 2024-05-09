package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

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

	public boolean exportCustomerRankingToCSV(List<CustomerRanking> customerRankingList, String folderPath) {
		boolean success = false;

		String filePath = folderPath + "/CustomerRanking.csv";

		try (FileWriter fileWriter = new FileWriter(filePath); CSVWriter csvWriter = new CSVWriter(fileWriter)) {

			String[] columnHeaders = { "CustomerID", "CustomerName", "PhoneNumber", "TotalSpending" };
			csvWriter.writeNext(columnHeaders);

			for (CustomerRanking customer : customerRankingList) {
				String[] rowData = { customer.getCustomerID(), customer.getCustomerName(), customer.getPhoneNumber(),
						String.valueOf(customer.getTotal()) };
				csvWriter.writeNext(rowData);
			}

			System.out.println("Export to file CSV at: " + folderPath);
			success = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

}
