package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.Account;
import entity.Room;

public class RoomDAO {
	
	private ConnectDB connectDB;

	public RoomDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<Room> getAllRoom() {
		Connection connection = connectDB.getConnection();
		PreparedStatement s = null;
		ResultSet rs = null;
		List<Room> roomList = null;

		try {
			s = connection.prepareStatement("SELECT RoomID, RoomName, NumberOfSeats FROM Room");
			rs = s.executeQuery();
			roomList = new ArrayList<Room>();
			while (rs.next()) {
				String roomID = rs.getString(1);
				String roomName = rs.getString(2);
				int numberOfSeats = rs.getInt(3);
				roomList.add(new Room(roomID, roomName, numberOfSeats));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(s, rs);
		}

		return roomList;
	}

	public Room getRoomByID(String roomIDToFind) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement("SELECT RoomID, RoomName, NumberOfSeats FROM Room");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String roomID = rs.getString(1);
				String roomName = rs.getString(2);
				int numberOfSeats = rs.getInt(3);
				return new Room(roomID, roomName, numberOfSeats);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
