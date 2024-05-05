package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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

}
