package com.SpringBootGuide.SpringRest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
/*
@Data is a Lombok annotation to create all the getters, setters, equals, hash, 
and toString methods, based on the fields.
*/
@Entity
/*
@Entity is a JPA annotation to make this object ready 
for storage in a JPA-based data store.
*/
public class Employee {
	
	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	private String role;
	
	public Employee() {
	
	}

	public Employee(String firstName, String lastName, String role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
	
	public String getName() {
		return this.firstName + " " + this.lastName;
	}
	
	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
	}
		
}
