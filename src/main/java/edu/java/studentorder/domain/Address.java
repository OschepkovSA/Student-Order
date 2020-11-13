package edu.java.studentorder.domain;

public class Address {
	private String postCode;
	private Street street;
	private String building;
	private String extension;
	private String appartment;
	
	public Address(String postCode , Street street , String building , String extension , String appartment) {
		this.postCode = postCode;
		this.street = street;
		this.building = building;
		this.extension = extension;
		this.appartment = appartment;
	}
	
	public Address() {
	
	}
	
	public void setStreet(Street street) {
		this.street = street;
	}
	
	public Street getStreet() {
		return street;
	}
	
	public String getBuilding() {
		return building;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String getAppartment() {
		return appartment;
	}
	
	public String getPostCode() {
		return postCode;
	}
	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public void setBuilding(String building) {
		this.building = building;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public void setAppartment(String appartment) {
		this.appartment = appartment;
	}
	
	@Override
	public String toString() {
		return "Address{" + "postCode='" + postCode + '\''
				+ ", street=" + street
				+ ", building='" + building + '\''
				+ ", extension='" + extension + '\''
				+ ", appartment='" + appartment + '\'' + '}';
	}
}
