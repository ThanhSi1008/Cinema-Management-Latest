package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		String orderID = null;
		String querySQL = "{CALL AddNewOrder(?, ?, ?, ?, ?, ?, ?)}";
		try {
			connection = connectDB.getConnection();
			callableStatement = connection.prepareCall(querySQL);

			callableStatement.setString(1, newOrder.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00")));
			callableStatement.setInt(2, newOrder.getQuantityTicket());
			callableStatement.setString(3, newOrder.getNote());
			callableStatement.setString(4, newOrder.getCustomer().getCustomerID());
			callableStatement.setString(5, newOrder.getEmployee().getEmployeeID());
			callableStatement.setString(6, newOrder.getSchedule().getScheduleID());
			callableStatement.registerOutParameter(7, Types.CHAR);

			callableStatement.execute();
			orderID = callableStatement.getString(7);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(callableStatement, rs);
		}
		return orderID;
	}

}
