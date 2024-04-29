package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import connectDB.ConnectDB;
import entity.Account;

public class AccountDAO {
	private ConnectDB connectDB;

	public AccountDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public Account getAccountByUsername(String username) {
		Account account = null;
		Connection connection = connectDB.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Account WHERE Username = ?");
			statement.setString(1, username);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				account = new Account();
				account.setUsername(resultSet.getString(2));
				account.setPassword(resultSet.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}

		return account;
	}

	public boolean createAccount(Account account) {
		PreparedStatement statement = null;
		int n = 0;

		try {
			String sql = "INSERT INTO Account (Username, Password, EmployeeID) VALUES (?, ?, ?)";
			statement = connectDB.getConnection().prepareStatement(sql);
			statement.setString(1, account.getUsername());
			statement.setString(2, BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
			statement.setString(3, account.getEmp().getEmployeeID());
			n = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, null);
		}

		return n > 0;
	}
	
	public String getRoleByUsername(String username) {
		String role = null;
		Connection connection = connectDB.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			String sqlQuery = "SELECT * FROM Emp_Acc(?)";
			statement = connection.prepareStatement(sqlQuery);
			statement.setString(1, username);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				role = resultSet.getString("Role"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}

		return role;
	}

}
