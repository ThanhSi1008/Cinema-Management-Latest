package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.Movie;
import entity.MovieSchedule;
import entity.Room;

public class MovieScheduleDAO {

	private ConnectDB connectDB;
	private MovieDAO movieDAO;
	private RoomDAO roomDAO;

	public MovieScheduleDAO() {
		movieDAO = new MovieDAO();
		roomDAO = new RoomDAO();
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<MovieSchedule> getAllMovieSchedule() {
		Connection connection = connectDB.getConnection();
		List<MovieSchedule> movieScheduletList = new ArrayList<MovieSchedule>();
		try {
			PreparedStatement s = connection.prepareStatement("SELECT ScheduleID, ScreeningTime, EndTime, MovieID, RoomID, PerSeatPrice from MovieSchedule");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String movieScheduleID = rs.getString(1);
				LocalDateTime screeningTime = rs.getTimestamp(2).toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp(3).toLocalDateTime();
				Movie movie = movieDAO.getMovieByID(rs.getString(3));
				Room room = roomDAO.getRoomByID(rs.getString(4));
				Double perSeatPrice = rs.getDouble(5);
				movieScheduletList.add(new MovieSchedule(movieScheduleID, screeningTime, endTime, movie, room, perSeatPrice));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieScheduletList;
	}

}
