package entity;

public class MovieScheduleSeat {
	private String movieSheduleSeatID;
	private boolean sold;
	private Seat seat;
	private MovieSchedule schedule;

	public MovieScheduleSeat(String movieSheduleSeatID, boolean sold, Seat seat, MovieSchedule schedule) {
		super();
		this.movieSheduleSeatID = movieSheduleSeatID;
		this.sold = sold;
		this.seat = seat;
		this.schedule = schedule;
	}

	public String getMovieSheduleSeatID() {
		return movieSheduleSeatID;
	}

	public void setMovieSheduleSeatID(String movieSheduleSeatID) {
		this.movieSheduleSeatID = movieSheduleSeatID;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public MovieSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(MovieSchedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		return "MovieScheduleSeat [movieSheduleSeatID=" + movieSheduleSeatID + ", sold=" + sold + ", seat=" + seat
				+ ", schedule=" + schedule + "]";
	}

}
