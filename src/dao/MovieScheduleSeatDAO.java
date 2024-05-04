package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.MovieSchedule;
import entity.MovieScheduleSeat;
import entity.Room;
import entity.Seat;
import entity.SeatType;

public class MovieScheduleSeatDAO {

	private ConnectDB connectDB;

	public MovieScheduleSeatDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<MovieScheduleSeat> getAllMovieScheduleSeatByMovieScheduleID(String scheduleIDToFind) {
		Connection connection = connectDB.getConnection();
		List<MovieScheduleSeat> movieScheduleSeatList = null;
		try {
			PreparedStatement s = connection.prepareStatement(
					"select * from moviescheduleseat mss join seat s on mss.seatid  = s.seatid join room r on s.roomid = r.roomid join seattype st on s.seattypeid = st.seattypeid where scheduleid = ?");
			s.setString(1, scheduleIDToFind);
			ResultSet rs = s.executeQuery();
			movieScheduleSeatList = new ArrayList<MovieScheduleSeat>();
			while (rs.next()) {
				String seatTypeID = rs.getString(11);
				String seatTypeName = rs.getString(12);
				String descriptionSeat = rs.getString(13);
				SeatType seatType = new SeatType(seatTypeID, seatTypeName, descriptionSeat);

				String roomID = rs.getString(8);
				String roomName = rs.getString(9);
				int numberOfSeats = rs.getInt(10);
				Room room = new Room(roomID, roomName, numberOfSeats);

				String seatID = rs.getString(4);
				String seatLocation = rs.getString(5);
				Seat seat = new Seat(seatID, seatLocation, room, seatType);

				boolean sold = rs.getBoolean(1);
				MovieScheduleSeat movieScheduleSeat = new MovieScheduleSeat(sold, seat,
						new MovieSchedule(scheduleIDToFind));

				movieScheduleSeatList.add(movieScheduleSeat);
			}
			return movieScheduleSeatList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieScheduleSeatList;
	}

	public void updateSeatTaken(String seatID, String scheduleID) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection
					.prepareStatement("update moviescheduleseat set sold = 1 where seatID = ? and scheduleid = ?");
			s.setString(1, seatID);
			s.setString(2, scheduleID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
