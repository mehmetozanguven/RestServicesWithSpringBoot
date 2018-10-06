package com.SpringBootGuide.SpringRest.exceptionHandler;

public class EmployeeNotFoundException extends RuntimeException{

	public EmployeeNotFoundException(String message) {
		super("Employee with id :" + message + " is not found");
	}
	
}
