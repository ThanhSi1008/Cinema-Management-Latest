package entity;

public class MovieRanking {

	private String movieID;
	private String movieName;
	private int views;
	private double revenue;

	public MovieRanking(String movieID, String movieName, int views, double revenue) {
		super();
		this.movieID = movieID;
		this.movieName = movieName;
		this.views = views;
		this.revenue = revenue;
	}

	public String getMovieID() {
		return movieID;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	@Override
	public String toString() {
		return "MovieRanking [movieID=" + movieID + ", movieName=" + movieName + ", views=" + views + ", revenue="
				+ revenue + "]";
	}
}
