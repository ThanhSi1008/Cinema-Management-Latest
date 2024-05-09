package gui.application.form.other.screening;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import dao.MovieScheduleDAO;
import entity.MovieSchedule;

public class ScreeningTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<MovieSchedule> movieScheduleList;
	private String[] columnNames = { "Movie Schedule ID", "Movie Name", "Duration", "Screening Time" };
	private MovieScheduleDAO movieScheduleDAO;	

	public ScreeningTableModel() {
		movieScheduleDAO = new MovieScheduleDAO();
		movieScheduleList = movieScheduleDAO.getAllMovieSchedule();
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
		return movieScheduleList.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MovieSchedule movieSchedule = movieScheduleList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return movieSchedule.getScheduleID();
			case 1:
				return movieSchedule.getMovie().getMovieName();
			case 2:
				return movieSchedule.getMovie().getDuration();
			case 3:
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				return movieSchedule.getScreeningTime().format(formatter);
		}
		return null;
	}

	public void setMovieScheduleList(List<MovieSchedule> movieScheduleList) {
		this.movieScheduleList = movieScheduleList;	
	}

}
