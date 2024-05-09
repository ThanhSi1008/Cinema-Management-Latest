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
import entity.ProductRanking;

public class ProductRankingDAO {

	private ConnectDB connectDB;

	public ProductRankingDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<ProductRanking> getProductRankingByMonthAndYear(int month, int year) {
		Connection connection = null;
		List<ProductRanking> productRankingList = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT * FROM fn_ProductSalesByYearMonthDay(?, ?, null) ORDER BY totalprice desc";
		try {
			productRankingList = new ArrayList<ProductRanking>();
			connection = connectDB.getConnection();
			s = connection.prepareStatement(sqlQuery);
			s.setInt(1, year);
			if (month == 0) {
				s.setNull(2, Types.INTEGER);
			} else {
				s.setInt(2, month);
			}
			rs = s.executeQuery();
			while (rs.next()) {
				String productName = rs.getString(1);
				int salesQty = rs.getInt(2);
				double revenue = rs.getDouble(3);
				productRankingList.add(new ProductRanking(productName, salesQty, revenue));
			}
			return productRankingList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(s, rs);
		}
		return productRankingList;
	}

	public boolean exportProductRankingToCSV(List<ProductRanking> productRankingList, String folderPath) {
		boolean success = false;

		String filePath = folderPath + "/ProductRanking.csv";

		try (FileWriter fileWriter = new FileWriter(filePath); CSVWriter csvWriter = new CSVWriter(fileWriter)) {

			String[] columnHeaders = { "ProductName", "SalesQuantity", "Revenue" };
			csvWriter.writeNext(columnHeaders);

			for (ProductRanking product : productRankingList) {
				String[] rowData = { product.getProductName(), String.valueOf(product.getSalesQty()),
						String.valueOf(product.getTotalRevenue()) };
				csvWriter.writeNext(rowData);
			}

			System.out.println("Export to file CSV at: " + folderPath);
			success = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return success;
	}

	public List<ProductRanking> getAllProductsSoldEachDay(int year, int month, int day) {
		Connection connection = null;
		List<ProductRanking> productRankingList = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT * FROM fn_ProductSalesByYearMonthDay(?, ?, ?) ORDER BY totalprice desc";
		try {
			productRankingList = new ArrayList<ProductRanking>();
			connection = connectDB.getConnection();
			s = connection.prepareStatement(sqlQuery);

			s.setInt(1, year);
			s.setInt(2, month);
			s.setInt(3, day);

			rs = s.executeQuery();
			while (rs.next()) {
				String productName = rs.getString(1);
				int salesQty = rs.getInt(2);
				double revenue = rs.getDouble(3);
				productRankingList.add(new ProductRanking(productName, salesQty, revenue));
			}
			return productRankingList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(s, rs);
		}
		productRankingList.add(new ProductRanking("", 0, 0));
		return productRankingList;
	}

}
