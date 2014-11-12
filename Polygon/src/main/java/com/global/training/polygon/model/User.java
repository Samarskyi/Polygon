package com.global.training.polygon.model;

/**
 * Created by eugenii.samarskyi on 12.11.2014.
 */
public class User {


	int id;
	String zone;
	String first_name;
	String lastName;

	public User(int id, String zone, String first_name, String lastName) {
		this.id = id;
		this.zone = zone;
		this.first_name = first_name;
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
