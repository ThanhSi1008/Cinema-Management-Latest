package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import connectDB.ConnectDB;

public class EmployeeDAO {

	private ConnectDB connectDB;

	public EmployeeDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public boolean updateAvatar(String imagePath, String employeeID) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		boolean success = false;

		try {
			connection = connectDB.getConnection();
			String sql = "UPDATE Employee SET ImageSource = ? WHERE EmployeeID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, imagePath);
			preparedStatement.setString(2, employeeID);

			int rowsUpdated = preparedStatement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Avatar updated successfully.");
				success = true;
			} else {
				System.out.println("Failed to update avatar.");
			}
		} catch (SQLException e) {
			System.out.println("Error updating avatar: " + e.getMessage());
		} finally {
			connectDB.close(preparedStatement, null);
		}

		return success;
	}

}
