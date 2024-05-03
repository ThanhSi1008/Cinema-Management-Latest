package entity;

public class Room {
	private String roomID;
	private String roomName;
	private int numberOfSeats;

	public Room() {
		super();
	}

	public Room(String roomName, int numberOfSeats) {
		super();
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
		return "Room [numberOfSeats=" + numberOfSeats + ", roomID=" + roomID + ", roomName=" + roomName + "]";
	}

}
