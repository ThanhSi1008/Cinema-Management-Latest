package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.opencsv.CSVWriter;

import connectDB.ConnectDB;
import entity.TotalSpending;

public class TotalSpendingDAO {
	private ConnectDB connectDB;

	public TotalSpendingDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public TotalSpending getTotalSpending(int year, int month) {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		String querySQL = "SELECT * FROM fn_CalculateTotalRevenueByMonthYear(?, ?)";
		TotalSpending totalSpending = null;
		try {
			connection = connectDB.getConnection();
			s = connection.prepareStatement(querySQL);
			s.setInt(1, year);
			if (month == 0) {
				s.setNull(2, Types.INTEGER);
			} else {
				s.setInt(2, month);
			}
			rs = s.executeQuery();

			if (rs.next()) {
				BigDecimal totalAddProduct = rs.getBigDecimal("TotalAddProduct").setScale(2, RoundingMode.HALF_UP);
				BigDecimal totalImportProduct = rs.getBigDecimal("TotalImportProduct").setScale(2,
						RoundingMode.HALF_UP);
				BigDecimal totalAddMovie = rs.getBigDecimal("TotalAddMovie").setScale(2, RoundingMode.HALF_UP);

				totalSpending = new TotalSpending(totalAddProduct, totalImportProduct, totalAddMovie);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(s, rs);
		}
		return totalSpending;
	}

	public boolean exportOrderAddProduct(String folderName) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		boolean success = false;
		String querySQL = "SELECT AddProductID, AddProductDate, Quantity, UnitPurchasePrice, Total, ProductID FROM OrderAddProduct ORDER BY AddProductDate DESC";
		try {
			connection = connectDB.getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery();

			String filePath = folderName + "/OrderAddProduct.csv";
			FileWriter fileWriter = new FileWriter(filePath);
			CSVWriter csvWriter = new CSVWriter(fileWriter);

			String[] columnHeaders = { "AddProductID", "AddProductDate", "Quantity", "UnitPurchasePrice", "Total",
					"ProductID" };
			csvWriter.writeNext(columnHeaders);

			while (resultSet.next()) {
				String[] rowData = { resultSet.getString("AddProductID"), resultSet.getString("AddProductDate"),
						resultSet.getString("Quantity"), resultSet.getString("UnitPurchasePrice"),
						resultSet.getString("Total"), resultSet.getString("ProductID") };
				csvWriter.writeNext(rowData);
			}

			csvWriter.close();
			fileWriter.close();

			System.out.println("Data has been successfully exported to the CSV file");

			success = true;

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}
		return success;
	}

	public boolean exportOrderImportProduct(String folderName) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		boolean success = false;
		String querySQL = "SELECT ImportProductID, ImportProductDate, Quantity, UnitPurchasePrice, Total, ProductID FROM OrderImportProduct ORDER BY ImportProductDate DESC";
		try {
			connection = connectDB.getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery();

			String filePath = folderName + "/OrderImportProduct.csv";
			FileWriter fileWriter = new FileWriter(filePath);
			CSVWriter csvWriter = new CSVWriter(fileWriter);

			String[] columnHeaders = { "ImportProductID", "ImportProductDate", "Quantity", "UnitPurchasePrice", "Total",
					"ProductID" };
			csvWriter.writeNext(columnHeaders);

			while (resultSet.next()) {
				String[] rowData = { resultSet.getString("ImportProductID"), resultSet.getString("ImportProductDate"),
						resultSet.getString("Quantity"), resultSet.getString("UnitPurchasePrice"),
						resultSet.getString("Total"), resultSet.getString("ProductID") };
				csvWriter.writeNext(rowData);
			}

			csvWriter.close();
			fileWriter.close();

			System.out.println("Data has been successfully exported to the CSV file");

			success = true;

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}
		return success;
	}

	public boolean exportOrderAddMovie(String folderName) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		boolean success = false;
		String querySQL = "SELECT AddMovieID, AddMovieDate, Total, MovieID FROM OrderAddMovie ORDER BY AddMovieDate DESC";
		try {
			connection = connectDB.getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery();

			String filePath = folderName + "/OrderAddMovie.csv";
			FileWriter fileWriter = new FileWriter(filePath);
			CSVWriter csvWriter = new CSVWriter(fileWriter);

			String[] columnHeaders = { "AddMovieID", "AddMovieDate", "Total", "MovieID" };
			csvWriter.writeNext(columnHeaders);

			while (resultSet.next()) {
				String[] rowData = { resultSet.getString("AddMovieID"), resultSet.getString("AddMovieDate"),
						resultSet.getString("Total"), resultSet.getString("MovieID") };
				csvWriter.writeNext(rowData);
			}

			csvWriter.close();
			fileWriter.close();

			System.out.println("Data has been successfully exported to the CSV file");

			success = true;

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}
		return success;
	}

	public boolean exportOrderSummary(String folderName) {
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		boolean success = false;
		String querySQL = "SELECT Time, TotalOrder, TotalSeat, TotalSeatValue, TotalProduct, TotalProductValue, TotalOrderValue FROM OrderSummaryView ORDER BY Time";
		try {
			connection = connectDB.getConnection();
			statement = connection.prepareStatement(querySQL);
			resultSet = statement.executeQuery();

			String filePath = folderName + "/OrderSummary.csv";
			FileWriter fileWriter = new FileWriter(filePath);
			CSVWriter csvWriter = new CSVWriter(fileWriter);

			String[] columnHeaders = { "Time", "TotalOrder", "TotalSeat", "TotalSeatValue", "TotalProduct",
					"TotalProductValue", "TotalOrderValue" };
			csvWriter.writeNext(columnHeaders);

			while (resultSet.next()) {
				String[] rowData = { resultSet.getString("Time"), resultSet.getString("TotalOrder"),
						resultSet.getString("TotalSeat"), resultSet.getString("TotalSeatValue"),
						resultSet.getString("TotalProduct"), resultSet.getString("TotalProductValue"),
						resultSet.getString("TotalOrderValue") };
				csvWriter.writeNext(rowData);
			}

			csvWriter.close();
			fileWriter.close();

			System.out.println("Data has been successfully exported to the CSV file");

			success = true;

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}
		return success;
	}

}
