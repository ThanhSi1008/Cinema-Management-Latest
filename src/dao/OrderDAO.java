package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import connectDB.ConnectDB;
import entity.Order;

public class OrderDAO {

	private ConnectDB connectDB;

	public OrderDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public String addNewOrder(Order newOrder) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement(
					"insert into [order] (orderdate, quantityseat, note, customerid, employeeid, scheduleid) output inserted.orderid values (?, ?, ?, ?, ?, ?)");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
			s.setString(1, newOrder.getOrderDate().format(formatter));
			s.setInt(2, newOrder.getQuantityTicket());
			s.setString(3, newOrder.getNote());
			s.setString(4, newOrder.getCustomer().getCustomerID());
			s.setString(5, newOrder.getEmployee().getEmployeeID());
			s.setString(6, newOrder.getSchedule().getScheduleID());
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
