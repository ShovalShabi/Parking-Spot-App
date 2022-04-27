package model;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Address {
	private String country;
	private String city;
	private String street;
	private String numHouse;
	private Coordinate coordinate;

	public Address(String country, String city, String street, String numHouse, Coordinate coordinate) {
		this.country = country;
		this.city = city;
		this.street = street;
		this.numHouse = numHouse;
		this.coordinate = coordinate;
	}

	public final String getCountry() {
		return country;
	}

	public final void setCountry(String country) {
		this.country = country;
	}

	public final String getCity() {
		return city;
	}

	public final void setCity(String city) {
		this.city = city;
	}

	public final String getStreet() {
		return street;
	}

	public final void setStreet(String street) {
		this.street = street;
	}

	public final String getNumHouse() {
		return numHouse;
	}

	public final void setNumHouse(String numHouse) {
		this.numHouse = numHouse;
	}

	public final Coordinate getCoordinate() {
		return coordinate;
	}

	public final void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Address) {
			Address other = (Address) obj;
			if (other.getCoordinate().getLat() == this.getCoordinate().getLat()
					&& other.getCoordinate().getLon() == this.getCoordinate().getLon()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return  country  +" "+ city +" "+ street +" "+ numHouse;
	}

}
