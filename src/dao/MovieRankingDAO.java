package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import connectDB.ConnectDB;
import entity.MovieRanking;

public class MovieRankingDAO {
	private ConnectDB connectDB;

	public MovieRankingDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<MovieRanking> getMovieRankingByMonthAndYear(int month, int year) {
		Connection connection = connectDB.getConnection();
		List<MovieRanking> movieRankingList = null;
		try {
			PreparedStatement s = connection
					.prepareStatement("SELECT * FROM fn_MovieSalesByYearMonth(?, ?) order by ViewTotal desc");
			s.setInt(1, year);
			if (month == 0) {
				s.setNull(2, Types.INTEGER);
			} else {
				s.setInt(2, month);
			}
			ResultSet rs = s.executeQuery();
			movieRankingList = new ArrayList<MovieRanking>();
			while (rs.next()) {
				String movieID = rs.getString(1);
				String movieName = rs.getString(2);
				int views = rs.getInt(3);
				double revenue = rs.getDouble(4);
				movieRankingList.add(new MovieRanking(movieID, movieName, views, revenue));
			}
			return movieRankingList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieRankingList;
	}

	public boolean exportMovieRankingToCSV(List<MovieRanking> movieRankingList, String folderPath) {
		boolean success = false;

		String filePath = folderPath + "/MovieRanking.csv";

		try (FileWriter fileWriter = new FileWriter(filePath); CSVWriter csvWriter = new CSVWriter(fileWriter)) {

			String[] columnHeaders = { "MovieID", "MovieName", "Views", "Revenue" };
			csvWriter.writeNext(columnHeaders);

			for (MovieRanking movie : movieRankingList) {
				String[] rowData = { movie.getMovieID(), movie.getMovieName(), String.valueOf(movie.getViews()),
						String.valueOf(movie.getRevenue()) };
				csvWriter.writeNext(rowData);
			}

			System.out.println("Export to file CSV at: " + folderPath);
			success = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

}
