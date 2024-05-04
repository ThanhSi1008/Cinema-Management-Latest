package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.OrderDetail;

public class OrderDetailDAO {

	private ConnectDB connectDB;

	public OrderDetailDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public boolean addNewOrderDetail(OrderDetail chosenProductOrderDetail) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection
					.prepareStatement("insert into orderdetail (Quantity, OrderID, ProductID) values (?, ?, ?)");
			s.setInt(1, chosenProductOrderDetail.getQuantity());
			s.setString(2, chosenProductOrderDetail.getOrder().getOrderID());
			s.setString(3, chosenProductOrderDetail.getProduct().getProductID());
			int rowsAffected = s.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
