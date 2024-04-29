package entity;

import java.time.LocalDateTime;

public class OrderAddMovie {
	private String addMovieID;
	private LocalDateTime addMovieDate;
	private double total;
	private Movie movie;

	public OrderAddMovie(LocalDateTime addMovieDate, Movie movie) {
		super();
		this.addMovieDate = addMovieDate;
		setTotal();
		this.movie = movie;
	}

	public OrderAddMovie() {
		super();
	}

	public LocalDateTime getAddMovieDate() {
		return addMovieDate;
	}

	public void setAddMovieDate(LocalDateTime addMovieDate) {
		this.addMovieDate = addMovieDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal() {
		// viet phuong thuc
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getAddMovieID() {
		return addMovieID;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "OrderAddMovie [addMovieID=" + addMovieID + ", addMovieDate=" + addMovieDate + ", total=" + total
				+ ", movie=" + movie + "]";
	}

}
