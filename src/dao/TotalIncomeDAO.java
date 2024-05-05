package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.TotalIncome;

public class TotalIncomeDAO {
	private ConnectDB connectDB;

	public TotalIncomeDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public TotalIncome getTotalIncome(int year, int month) {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement s = null;
		String querySQL = "select * from CalculateTotalOrderAmountByYearMonth(?, ?)";
		TotalIncome totalIncome = null;
		try {
			connection = connectDB.getConnection();
			s = connection.prepareStatement(querySQL);
			s.setInt(1, year);
			s.setInt(2, month);

			rs = s.executeQuery();

			if (rs.next()) {
				BigDecimal totalProduct = rs.getBigDecimal("TotalProduct").setScale(2, RoundingMode.HALF_UP);
				BigDecimal totalSeat = rs.getBigDecimal("TotalSeat").setScale(2, RoundingMode.HALF_UP);

				totalIncome = new TotalIncome(totalProduct, totalSeat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(s, rs);
		}
		return totalIncome;
	}

}
