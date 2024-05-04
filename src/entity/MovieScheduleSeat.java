package entity;

import java.io.Serializable;
import java.util.Objects;

public class MovieScheduleSeat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean sold;
	private Seat seat;
	private MovieSchedule movieSchedule;

	public MovieScheduleSeat(boolean sold, Seat seat, MovieSchedule movieSchedule) {
		super();
		this.sold = sold;
		this.seat = seat;
		this.movieSchedule = movieSchedule;
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

	public MovieSchedule getMovieSchedule() {
		return movieSchedule;
	}

	public void setMovieSchedule(MovieSchedule movieSchedule) {
		this.movieSchedule = movieSchedule;
	}

	@Override
	public String toString() {
		return seat.getSeatLocation();
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieSchedule, seat);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieScheduleSeat other = (MovieScheduleSeat) obj;
		return Objects.equals(movieSchedule, other.movieSchedule) && Objects.equals(seat, other.seat);
	}

}
