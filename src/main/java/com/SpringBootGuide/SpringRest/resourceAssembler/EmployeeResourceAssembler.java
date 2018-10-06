package com.SpringBootGuide.SpringRest.resourceAssembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.SpringBootGuide.SpringRest.controller.EmployeeController;
import com.SpringBootGuide.SpringRest.entity.Employee;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {
	// Now we added HATEOS to make our web service as a REST service.
	// Via HATEOS, our return doesn't include only data, also includes links
	@Override
	public Resource<Employee> toResource(Employee employee) {
		return new Resource<>(employee,
				linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
		// with the first one, we tell the Spring HATEOS build a link to the
		// EmployeeController's getEmployee() method, and flag it as a self link
		// Self link indicates that "this link is an identifier for its context"
		// build a link to getAllEmployees() and call it "employees"
	}

}
