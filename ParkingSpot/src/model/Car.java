package model;

import exceptions.UnvalidCarPlateException;

public class Car {

	private int ID;
	private String manufacturer;
	private String model;
	private String color;

	public Car(int plateNumber, String manufacturer, String model, String color) throws UnvalidCarPlateException {
		this.manufacturer = manufacturer;
		this.model = model;
		this.color = color;
		this.checkCarId(plateNumber);
		this.ID=plateNumber;
	}

	public int getID() {
		return ID;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModel() {
		return model;
	}

	public String getColor() {
		return color;
	}
	
	public void  checkCarId(int id) throws UnvalidCarPlateException {
		if(id<1000000 || id>99999999)throw new UnvalidCarPlateException();
	}

	@Override
	public String toString() {
		return "Car's ID is: " + ID + "\n manufactor: " + manufacturer + "\n model: " + model + "color: " + color;
	}

}
