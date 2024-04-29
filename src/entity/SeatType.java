package entity;

public class SeatType {
	private String seatTypeID;
	private String seatTypeName;
	private String descriptionSeat;

	public SeatType() {
		super();
	}

	public SeatType(String seatTypeName, String descriptionSeat) {
		super();
		this.seatTypeName = seatTypeName;
		this.descriptionSeat = descriptionSeat;
	}

	public String getSeatTypeID() {
		return seatTypeID;
	}

	public String getSeatTypeName() {
		return seatTypeName;
	}

	public void setSeatTypeName(String seatTypeName) {
		this.seatTypeName = seatTypeName;
	}

	public String getDescriptionSeat() {
		return descriptionSeat;
	}

	public void setDescriptionSeat(String descriptionSeat) {
		this.descriptionSeat = descriptionSeat;
	}

	@Override
	public String toString() {
		return "SeatType [seatTypeID=" + seatTypeID + ", seatTypeName=" + seatTypeName + ", descriptionSeat="
				+ descriptionSeat + "]";
	}

}
