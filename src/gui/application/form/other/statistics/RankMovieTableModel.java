package gui.application.form.other.statistics;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.MovieRanking;

public class RankMovieTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<MovieRanking> movieRankingList;
	private String[] columnNames = { "Index", "Movie Name", "Views", "Revenue" };

	public RankMovieTableModel() {
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return movieRankingList.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MovieRanking movieRanking = movieRankingList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return movieRanking.getMovieName();
			case 2:
				return movieRanking.getViews();
			case 3:
				DecimalFormat df = new DecimalFormat("#0.00");
				return "$" + df.format(movieRanking.getRevenue());
		}
		return null;
	}
	
	public void setMovieRankingList(List<MovieRanking> movieRankingList) {
		this.movieRankingList = movieRankingList;
	}

}
