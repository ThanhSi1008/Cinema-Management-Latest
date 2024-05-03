package entity;

import java.time.LocalDateTime;

public class MovieSchedule {
	private String scheduleID;
	private LocalDateTime screeningTime;
	private LocalDateTime endTime;
	private Movie movie;
	private Room room;

	public MovieSchedule(LocalDateTime screeningTime, Movie movie, Room room) {
		super();
		this.screeningTime = screeningTime;
//		setEndTime();
		this.movie = movie;
		this.room = room;
	}

	public MovieSchedule() {
		super();
	}

	public String getScheduleID() {
		return scheduleID;
	}

	public LocalDateTime getScreeningTime() {
		return screeningTime;
	}

	public void setScreeningTime(LocalDateTime screeningTime) {
		this.screeningTime = screeningTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

//	// Cần xem lại method này!
//	public void setEndTime() {
//		if (screeningTime != null && movie.getDuration() != null) {
//			endTime = screeningTime.plus(movie.getDuration());
//		} else {
//			System.out.println("Không thể tính thời gian kết thúc!");
//		}
//	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "MovieSchedule [scheduleID=" + scheduleID + ", screeningTime=" + screeningTime + ", endTime=" + endTime
				+ ", movie=" + movie + ", room=" + room + "]";
	}

}
