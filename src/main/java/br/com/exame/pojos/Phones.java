package br.com.exame.pojos;

import com.google.gson.annotations.SerializedName;

public class Phones {

	@SerializedName("extension")
	private String extension;
	@SerializedName("id")
	private Integer id;
	@SerializedName("number")
	private String number;
	@SerializedName("phone_type")
	private String phoneType;

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

}
