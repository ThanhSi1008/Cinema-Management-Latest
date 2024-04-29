package entity;

public class Seat {
	private String seatID;
	private String seatLocation;
	private Room room;
	private SeatType seatType;

	public Seat(String seatLocation, Room room, SeatType seatType) {
		super();
		this.seatLocation = seatLocation;
		this.room = room;
		this.seatType = seatType;
	}

	public Seat() {
		super();
	}

	public String getSeatID() {
		return seatID;
	}

	public String getSeatLocation() {
		return seatLocation;
	}

	public void setSeatLocation(String seatLocation) {
		this.seatLocation = seatLocation;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}

	@Override
	public String toString() {
		return "Seat [seatID=" + seatID + ", seatLocation=" + seatLocation + ", room=" + room + ", seatType=" + seatType
				+ "]";
	}

}
