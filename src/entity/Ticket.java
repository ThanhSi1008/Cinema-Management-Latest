package entity;

public class Ticket {
	private String ticketID;
	private double ticketPrice;
	private boolean sold;
	private Seat seat;
	private MovieSchedule schedule;

	public Ticket() {
		super();
	}

	public Ticket(double ticketPrice, boolean sold, Seat seat, MovieSchedule schedule) {
		super();
		this.ticketPrice = ticketPrice;
		this.sold = sold;
		this.seat = seat;
		this.schedule = schedule;
	}

	public String getTicketID() {
		return ticketID;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
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
		return "Ticket [ticketID=" + ticketID + ", ticketPrice=" + ticketPrice + ", sold=" + sold + ", seat=" + seat
				+ ", schedule=" + schedule + "]";
	}

}
