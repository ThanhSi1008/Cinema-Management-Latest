package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ProductRanking;

public class ProductRankingDAO {

	private ConnectDB connectDB;
	
	public ProductRankingDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<ProductRanking> getProductRankingByMonthAndYear(int month, int year) {
		Connection connection = connectDB.getConnection();
		List<ProductRanking> productRankingList = null;
		try {
			productRankingList = new ArrayList<ProductRanking>();
			PreparedStatement s = connection.prepareStatement("SELECT * FROM fn_ProductSalesByYearMonth(?, ?) ORDER BY totalprice desc");
			s.setInt(1, year);
			if (month == 0) {
				s.setNull(2, Types.INTEGER);
			} else {
				s.setInt(2, month);
			}
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String productName = rs.getString(1);
				int salesQty = rs.getInt(2);
				double revenue = rs.getDouble(3);
				productRankingList.add(new ProductRanking(productName, salesQty, revenue));
			}
			return productRankingList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productRankingList;
	}
	
}
