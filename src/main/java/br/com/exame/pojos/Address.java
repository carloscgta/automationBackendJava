package br.com.exame.pojos;

import com.google.gson.annotations.SerializedName;

public class Address {
	
	@SerializedName("additional_details")
	private String additionalDetails;
	private String city;
	@SerializedName("country")
	private String country;
	@SerializedName("neighborhood")
	private String neighborhood;
	@SerializedName("number")
	private String number;
	@SerializedName("state")
	private String state;
	@SerializedName("street")
	private String street;
	@SerializedName("zipcode")
	private String zipcode;

	public String getAdditionalDetails() {
	return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
	this.additionalDetails = additionalDetails;
	}

	public String getCity() {
	return city;
	}

	public void setCity(String city) {
	this.city = city;
	}

	public String getCountry() {
	return country;
	}

	public void setCountry(String country) {
	this.country = country;
	}

	public String getNeighborhood() {
	return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
	this.neighborhood = neighborhood;
	}

	public String getNumber() {
	return number;
	}

	public void setNumber(String number) {
	this.number = number;
	}

	public String getState() {
	return state;
	}

	public void setState(String state) {
	this.state = state;
	}

	public String getStreet() {
	return street;
	}

	public void setStreet(String street) {
	this.street = street;
	}

	public String getZipcode() {
	return zipcode;
	}

	public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
	}

}
