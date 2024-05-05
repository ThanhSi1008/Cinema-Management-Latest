package dao;

import java.sql.Connection;

import connectDB.ConnectDB;
import entity.Income;

public class IncomeDAO {
	
	private ConnectDB connectDB;

	public IncomeDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}


}
