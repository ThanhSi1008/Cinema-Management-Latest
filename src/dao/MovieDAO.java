package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.Movie;

public class MovieDAO {

	private ConnectDB connectDB;

	public MovieDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<Movie> getAllMovie() {
		ArrayList<Movie> movieList = new ArrayList<Movie>();
		Connection connection = connectDB.getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement("SELECT MovieID, MovieName, Status, Duration FROM Movie");
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String movieID = resultSet.getString("MovieID");
				String name = resultSet.getString("MovieName");
				String status = resultSet.getString("Status");
				int duration = resultSet.getInt("duration");
				movieList.add(new Movie(movieID, name, status, duration));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}

		return movieList;
	}

	public boolean addNewMovie(Movie newMovie) {
		Connection connection = connectDB.getConnection();
		String insertSQL = "INSERT INTO Movie (MovieName, Description, Genre, Director, Duration, ReleasedDate, Language, Country, Trailer, StartDate, Status, ImportPrice, ImageSource)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, newMovie.getMovieName());
			preparedStatement.setString(2, newMovie.getDescription());
			preparedStatement.setString(3, newMovie.getGenre());
			preparedStatement.setString(4, newMovie.getDirector());
			preparedStatement.setInt(5, newMovie.getDuration());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			preparedStatement.setString(6, newMovie.getReleasedDate().format(formatter));
			preparedStatement.setString(7, newMovie.getLanguage());
			preparedStatement.setString(8, newMovie.getCountry());
			preparedStatement.setString(9, newMovie.getTrailer());
			preparedStatement.setString(10, newMovie.getStartDate().format(formatter));
			preparedStatement.setString(11, newMovie.getStatus());
			preparedStatement.setDouble(12, newMovie.getImportPrice());
			preparedStatement.setString(13, newMovie.getImageSource());
			int rowsAffected = preparedStatement.executeUpdate();
			System.out.println(rowsAffected + " row(s) inserted successfully.");
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int deleteMovieByID(String movieID) {
		Connection connection = connectDB.getConnection();
		String deleteSQL = "DELETE FROM Movie WHERE MovieID = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, movieID);
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public Movie getMovieByID(String movieID) {
		Connection connection = connectDB.getConnection();
		String selectByIDSQL = "SELECT * FROM Movie WHERE MovieID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(selectByIDSQL);
			preparedStatement.setString(1, movieID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String movieName = resultSet.getString(2);
				String description = resultSet.getString("Description");
				String genre = resultSet.getString(3);
				String director = resultSet.getString(4);
				int duration = resultSet.getInt("Duration");
				Date releasedDate = resultSet.getDate("ReleasedDate");
				String language = resultSet.getString("Language");
				String country = resultSet.getString("Country");
				String trailer = resultSet.getString("Trailer");
				Date startDate = resultSet.getDate("StartDate");
				String status = resultSet.getString("Status");
				double importPrice = resultSet.getDouble("ImportPrice");
				String imageSource = resultSet.getString("ImageSource");
				return new Movie(movieID, movieName, description, genre, director, duration, releasedDate.toLocalDate(),
						language, country, trailer, startDate.toLocalDate(), status, importPrice, imageSource);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int updateMovieByID(String movieID, Movie updatedMovie) {
		Connection connection = connectDB.getConnection();
		String updateSQL = "UPDATE Movie SET MovieName=?, Description=?, Genre=?, Director=?, Duration=?, ReleasedDate=?, Language=?, Country=?, Trailer=?, StartDate=?, Status=?, ImportPrice=?, ImageSource=? WHERE MovieID=?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, updatedMovie.getMovieName());
			preparedStatement.setString(2, updatedMovie.getDescription());
			preparedStatement.setString(3, updatedMovie.getGenre());
			preparedStatement.setString(4, updatedMovie.getDirector());
			preparedStatement.setInt(5, updatedMovie.getDuration());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			preparedStatement.setString(6, updatedMovie.getReleasedDate().format(formatter));
			preparedStatement.setString(7, updatedMovie.getLanguage());
			preparedStatement.setString(8, updatedMovie.getCountry());
			preparedStatement.setString(9, updatedMovie.getTrailer());
			preparedStatement.setString(10, updatedMovie.getStartDate().format(formatter));
			preparedStatement.setString(11, updatedMovie.getStatus());
			preparedStatement.setDouble(12, updatedMovie.getImportPrice());
			preparedStatement.setString(13, updatedMovie.getImageSource());
			preparedStatement.setString(14, movieID);
			int rowsAffected = preparedStatement.executeUpdate();
			System.out.println(rowsAffected + " row(s) inserted successfully.");
			return rowsAffected;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database failed");
			return 0;
		}
	}

	public List<Movie> getMovieByStatus(String statusToFind) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT MovieID, MovieName, Status, Duration FROM Movie WHERE STATUS = ?";
		List<Movie> movieList = new ArrayList<Movie>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, statusToFind);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String movieID = resultSet.getString(1);
				String movieName = resultSet.getString(2);
				String status = resultSet.getString(3);
				int duration = resultSet.getInt(4);
				movieList.add(new Movie(movieID, movieName, status, duration));
			}
			return movieList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Movie> findMovieByName(String movieNameToFind) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT MovieID, MovieName, Status, Duration FROM Movie WHERE MovieName LIKE ?";
		List<Movie> movieList = new ArrayList<Movie>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, "%" + movieNameToFind + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String movieID = resultSet.getString(1);
				String movieName = resultSet.getString(2);
				String status = resultSet.getString(3);
				int duration = resultSet.getInt(4);
				movieList.add(new Movie(movieID, movieName, status, duration));
			}
			return movieList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Movie> findMovieByNameAndStatus(String nameToFind, String statusToFind) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT MovieID, MovieName, Status, Duration FROM Movie WHERE MovieName LIKE ? AND STATUS = ?";
		List<Movie> movieList = new ArrayList<Movie>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, "%" + nameToFind + "%");
			preparedStatement.setString(2, statusToFind);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String movieID = resultSet.getString(1);
				String movieName = resultSet.getString(2);
				String status = resultSet.getString(3);
				int duration = resultSet.getInt(4);
				movieList.add(new Movie(movieID, movieName, status, duration));
			}
			return movieList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
