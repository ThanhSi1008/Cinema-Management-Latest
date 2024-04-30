package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;

import connectDB.ConnectDB;
import entity.Account;
import entity.Employee;

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

	public Employee getEmployeeByUsername(String username, boolean authentication) {
		Employee employee = null;
		Connection connection = connectDB.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sqlQuery = "SELECT * FROM getEmployeeByAccount(?)";

		if (getAccountByUsername(username) != null && authentication) {
			try {
				statement = connection.prepareStatement(sqlQuery);
				statement.setString(1, username);
				resultSet = statement.executeQuery();

				if (resultSet.next()) {
					String id = resultSet.getString("EmployeeID");
					String name = resultSet.getString("FullName");
					boolean gender = resultSet.getBoolean("Gender");
					LocalDate dob = resultSet.getDate("DateOfBirth").toLocalDate();
					String email = resultSet.getString("Email");
					String phone = resultSet.getString("PhoneNumber");
					String role = resultSet.getString("Role");
					LocalDate starting = resultSet.getDate("StartingDate").toLocalDate();
					double salary = resultSet.getDouble("Salary");
					String image = resultSet.getString("ImageSource");

					employee = new Employee(id, name, gender, dob, email, phone, role, starting, salary, image);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connectDB.close(statement, resultSet);
			}
		}

		return employee;
	}

}