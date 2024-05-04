package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		List<MovieSchedule> movieScheduleList = new ArrayList<MovieSchedule>();
		try {
			PreparedStatement s = connection.prepareStatement(
					"SELECT ScheduleID, ScreeningTime, EndTime, MovieID, RoomID, PerSeatPrice from MovieSchedule");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String movieScheduleID = rs.getString(1);
				LocalDateTime screeningTime = rs.getTimestamp(2).toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp(3).toLocalDateTime();
				Movie movie = movieDAO.getMovieByID(rs.getString(4));
				Room room = roomDAO.getRoomByID(rs.getString(5));
				Double perSeatPrice = rs.getDouble(6);
				movieScheduleList
						.add(new MovieSchedule(movieScheduleID, screeningTime, endTime, movie, room, perSeatPrice));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieScheduleList;
	}

	public List<MovieSchedule> getAllMovieScheduleByDate(LocalDate searchedDateLocalDate) {
		Connection connection = connectDB.getConnection();
		List<MovieSchedule> movieScheduleList = new ArrayList<MovieSchedule>();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			PreparedStatement s = connection.prepareStatement(
					"SELECT ScheduleID, ScreeningTime, EndTime, MovieID, RoomID, PerSeatPrice from MovieSchedule WHERE CAST(ScreeningTime AS DATE) = ?");
			s.setString(1, searchedDateLocalDate.format(formatter));
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String movieScheduleID = rs.getString(1);
				LocalDateTime screeningTime = rs.getTimestamp(2).toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp(3).toLocalDateTime();
				Movie movie = movieDAO.getMovieByID(rs.getString(4));
				Room room = roomDAO.getRoomByID(rs.getString(5));
				Double perSeatPrice = rs.getDouble(6);
				movieScheduleList
						.add(new MovieSchedule(movieScheduleID, screeningTime, endTime, movie, room, perSeatPrice));
			}
			return movieScheduleList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieScheduleList;
	}

	public List<MovieSchedule> findMovieScheduleByRoom(String roomNameToFind) {
		Connection connection = connectDB.getConnection();
		List<MovieSchedule> movieScheduleList = new ArrayList<MovieSchedule>();

		try {
			PreparedStatement s = connection.prepareStatement(
					"SELECT ScheduleID, ScreeningTime, EndTime, MovieID, RoomID, PerSeatPrice FROM MovieSchedule WHERE RoomID = (SELECT RoomID FROM Room WHERE RoomName = ?)");
			s.setString(1, roomNameToFind);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String movieScheduleID = rs.getString(1);
				LocalDateTime screeningTime = rs.getTimestamp(2).toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp(3).toLocalDateTime();
				Movie movie = movieDAO.getMovieByID(rs.getString(4));
				Room room = roomDAO.getRoomByID(rs.getString(5));
				Double perSeatPrice = rs.getDouble(6);
				movieScheduleList
						.add(new MovieSchedule(movieScheduleID, screeningTime, endTime, movie, room, perSeatPrice));
			}
			return movieScheduleList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieScheduleList;

	}

	public List<MovieSchedule> findMovieScheduleByRoomNameAndDate(String roomNameToFind,
			LocalDate searchedDateLocalDate) {
		Connection connection = connectDB.getConnection();
		List<MovieSchedule> movieScheduleList = new ArrayList<MovieSchedule>();

		try {
			PreparedStatement s = connection.prepareStatement(
					"SELECT ScheduleID, ScreeningTime, EndTime, MovieID, RoomID, PerSeatPrice FROM MovieSchedule WHERE RoomID = (SELECT RoomID FROM Room WHERE RoomName = ?) AND CAST(ScreeningTime AS DATE) = ?");
			s.setString(1, roomNameToFind);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			s.setString(2, searchedDateLocalDate.format(formatter));
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String movieScheduleID = rs.getString(1);
				LocalDateTime screeningTime = rs.getTimestamp(2).toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp(3).toLocalDateTime();
				Movie movie = movieDAO.getMovieByID(rs.getString(4));
				Room room = roomDAO.getRoomByID(rs.getString(5));
				Double perSeatPrice = rs.getDouble(6);
				movieScheduleList
						.add(new MovieSchedule(movieScheduleID, screeningTime, endTime, movie, room, perSeatPrice));
			}
			return movieScheduleList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieScheduleList;
	}

	public boolean addNewMovieSchedule(MovieSchedule movieSchedule) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement(
					"insert into movieschedule (screeningTime, movieid, roomid, perseatprice) values (?, ?, ?, ?)");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
			s.setString(1, movieSchedule.getScreeningTime().format(formatter));
			s.setString(2, movieSchedule.getMovie().getMovieID());
			s.setString(3, movieSchedule.getRoom().getRoomID());
			s.setDouble(4, movieSchedule.getPerSeatPrice());
			int rowsAffected = s.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public MovieSchedule getMovieScheduleByID(String movieScheduleIDToFind) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement(
					"SELECT ScheduleID, ScreeningTime, EndTime, MovieID, RoomID, PerSeatPrice from MovieSchedule WHERE ScheduleID = ?");
			s.setString(1, movieScheduleIDToFind);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				String movieScheduleID = rs.getString(1);
				LocalDateTime screeningTime = rs.getTimestamp(2).toLocalDateTime();
				LocalDateTime endTime = rs.getTimestamp(3).toLocalDateTime();
				Movie movie = movieDAO.getMovieByID(rs.getString(4));
				Room room = roomDAO.getRoomByID(rs.getString(5));
				Double perSeatPrice = rs.getDouble(6);
				return new MovieSchedule(movieScheduleID, screeningTime, endTime, movie, room, perSeatPrice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateMovieSchedule(MovieSchedule movieSchedule) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement(
					"update movieschedule set screeningTime = ?, movieid = ?, roomid = ?, perseatprice = ? where scheduleid = ?");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
			s.setString(1, movieSchedule.getScreeningTime().format(formatter));
			s.setString(2, movieSchedule.getMovie().getMovieID());
			s.setString(3, movieSchedule.getRoom().getRoomID());
			s.setDouble(4, movieSchedule.getPerSeatPrice());
			s.setString(5, movieSchedule.getScheduleID());
			int rowsAffected = s.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteMovieScheduleByID(String scheduleID) {
		Connection connection = connectDB.getConnection();
		try {
			PreparedStatement s = connection.prepareStatement("DELETE FROM MovieSchedule WHERE ScheduleID = ?");
			s.setString(1, scheduleID);
			int rowsAffected = s.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
