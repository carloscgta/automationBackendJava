package br.com.exame.pojos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Customer {
		
	@SerializedName("address")
	private Address address;
	@SerializedName("code")
	private String code;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("email")
	private String email;
	@SerializedName("id")
	private Integer id;
	@SerializedName("name")
	private String name;
	@SerializedName("notes")
	private String notes;
	@SerializedName("phones")
	private List<Phones> phones;
	@SerializedName("registry_code")
	private String registryCode;
	@SerializedName("status")
	private String status;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("userId")
	private Integer userId;
	
	@SerializedName("user_type")
	private String usertype;


	public Address getAddress() {
	return address;
	}

	public void setAddress(Address address) {
	this.address = address;
	}

	public String getCode() {
	return code;
	}

	public void setCode(String code) {
	this.code = code;
	}

	public String getCreatedAt() {
	return createdAt;
	}

	public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
	}

	public String getEmail() {
	return email;
	}

	public void setEmail(String email) {
	this.email = email;
	}

	public Integer getId() {
	return id;
	}

	public void setId(Integer id) {
	this.id = id;
	}

	public String getName() {
	return name;
	}

	public void setName(String name) {
	this.name = name;
	}

	public String getNotes() {
	return notes;
	}

	public void setNotes(String notes) {
	this.notes = notes;
	}
	
	public List<Phones> getPhones() {
		return phones;
	}

	public void setPhones(List<Phones> phones) {
		this.phones = phones;
	}
	

	public String getRegistryCode() {
	return registryCode;
	}

	public void setRegistryCode(String registryCode) {
	this.registryCode = registryCode;
	}

	public String getStatus() {
	return status;
	}

	public void setStatus(String status) {
	this.status = status;
	}

	public String getUpdatedAt() {
	return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
	this.updatedAt = updatedAt;
	}

	public Integer getUserId() {
	return userId;
	}

	public void setUserId(Integer userId) {
	this.userId = userId;
	}
	
	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

}
