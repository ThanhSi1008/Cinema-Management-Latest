package entity;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class Movie {
	private String movieID;
	private String movieName;
	private String description;
	private String genre;
	private String director;
	private int duration;
	private LocalDate releasedDate;
	private String language;
	private String country;
	private String trailer;
	private LocalDate startDate;
	private String status;
	private double importPrice;
	private String imageSource;

	public Movie() {
		super();
	}

	public Movie(String movieName, String description, String genre, String director, int duration,
			LocalDate releasedDate, String language, String country, String trailer, LocalDate startDate, String status,
			double importPrice, String imageSource) {
		super();
		this.movieName = movieName;
		this.description = description;
		this.genre = genre;
		this.director = director;
		this.duration = duration;
		this.releasedDate = releasedDate;
		this.language = language;
		this.country = country;
		this.trailer = trailer;
		this.startDate = startDate;
		this.status = status;
		this.importPrice = importPrice;
		this.imageSource = imageSource;
	}
	
	public Movie(String movieID, String movieName, String description, String genre, String director, int duration,
			LocalDate releasedDate, String language, String country, String trailer, LocalDate startDate, String status,
			double importPrice, String imageSource) {
		super();
		this.movieID = movieID;
		this.movieName = movieName;
		this.description = description;
		this.genre = genre;
		this.director = director;
		this.duration = duration;
		this.releasedDate = releasedDate;
		this.language = language;
		this.country = country;
		this.trailer = trailer;
		this.startDate = startDate;
		this.status = status;
		this.importPrice = importPrice;
		this.imageSource = imageSource;
	}

	public Movie(String movieID, String name, String status, int duration) {
		this.movieID = movieID;
		this.movieName = name;
		this.status = status;
		this.duration = duration;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getMovieID() {
		return movieID;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public LocalDate getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(LocalDate releasedDate) {
		this.releasedDate = releasedDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getImportPrice() {
		return importPrice;
	}

	public void setImportPrice(double importPrice) {
		this.importPrice = importPrice;
	}

	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	@Override
	public String toString() {
		return "Movie [movieID=" + movieID + ", movieName=" + movieName + ", description=" + description + ", genre="
				+ genre + ", director=" + director + ", duration=" + duration + ", releasedDate=" + releasedDate
				+ ", language=" + language + ", country=" + country + ", trailer=" + trailer + ", startDate="
				+ startDate + ", status=" + status + ", importPrice=" + importPrice + ", imageSource=" + imageSource
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(movieID, other.movieID);
	}

}
