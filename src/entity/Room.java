package entity;

public class Room {
	private String roomID;
	private String roomName;
	private int numberOfSeats;

	public Room(String roomID, String roomName, int numberOfSeats) {
		super();
		this.roomID = roomID;
		this.roomName = roomName;
		this.numberOfSeats = numberOfSeats;
	}

	public String getRoomID() {
		return roomID;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	@Override
	public String toString() {
		return roomName;
	}

}
