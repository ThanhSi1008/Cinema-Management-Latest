package entity;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(roomID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return Objects.equals(roomID, other.roomID);
	}

}
