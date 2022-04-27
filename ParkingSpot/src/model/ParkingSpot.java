package model;

public class ParkingSpot {

	private Address address;
	private boolean isTaken;

	public ParkingSpot(Address address) {
		this.address = address;
		this.isTaken = false;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public final boolean isTaken() {
		return isTaken;
	}

	public final void setTaken(boolean taken) {
		this.isTaken = taken;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParkingSpot) {
			ParkingSpot other = (ParkingSpot) obj;
			if (other.getAddress().equals(this.getAddress())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "ParkingSpot [address=" + address + ", taken=" + isTaken;
	}
}
