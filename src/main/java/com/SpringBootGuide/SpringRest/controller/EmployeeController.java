package com.SpringBootGuide.SpringRest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.SpringBootGuide.SpringRest.dao.EmployeeRepository;
import com.SpringBootGuide.SpringRest.entity.Employee;
import com.SpringBootGuide.SpringRest.exceptionHandler.EmployeeNotFoundException;
import com.SpringBootGuide.SpringRest.resourceAssembler.EmployeeResourceAssembler;
/**
 * HTTP Methods
 * 		HTTP GET 	= (READ) 200 (OK), 404 (Not Found), if ID not found or invalid.
 * 		HTTP POST 	= (CREATE) 201 (Created), 404 (Not Found), 409 (Conflict) if resource already exists.
 * 		HTTP PUT	= (UPDATE/REPLACE) 405 (Method Not Allowed), unless you want to update/replace every resource in the entire collection. 200 (OK) or 204 (No Content). 404 (Not Found), if ID not found or invalid.
 * 		HTTP DELETE	= (DELETE) 405 (Method Not Allowed), unless you want to delete the whole collection—not often desirable. 200 (OK). 404 (Not Found), if ID not found or invalid.
 * @author mehmetozanguven
 *
 */
@RestController
public class EmployeeController {

	private EmployeeRepository employeeRepository;
	private EmployeeResourceAssembler employeeResourceAssembler;
	
	public EmployeeController(EmployeeRepository employeeRepository, EmployeeResourceAssembler employeeResourceAssembler) {
		this.employeeRepository = employeeRepository;
		this.employeeResourceAssembler = employeeResourceAssembler;
	}
	
	// - Annotation for mapping HTTP GET requests onto specific handler methods.
	// - Specifically, @GetMapping is a composed annotation that acts as a 
	// shortcut for @RequestMapping(method = RequestMethod.GET).
	// - Overall, When we get HTTP GET request this method will work and
	// returns list of all employees, then it will automatically convert the
	// appropriate JSON object
	// NOTE THAT: produces is necessary otherwise get an error javax.xml.bind.JAXBException: class ... nor any of its super class is known to this context
	@RequestMapping(value = "/employees", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	//@GetMapping("/employees")
	public Resources<Resource<Employee>> getAllEmployees() {
		List<Resource<Employee>> employees = employeeRepository.findAll().stream()
				.map(employeeResourceAssembler::toResource)
				.collect(Collectors.toList());

			return new Resources<>(employees,
				linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());

	}
	
	// - Annotation for mapping HTTP POST requests onto specific handler methods.
	// - Specifically, @PostMapping is a composed annotation that acts as a 
	// shortcut for @RequestMapping(method = RequestMethod.POST).
	// - Overall, When we get HTTP POST request this method will work
	// and take the data from HTTP request body, bind them to newEmployee
	// then save it to the H2 database, finally return the saved employee
	// as a JSON object to the browser(client).
	@PostMapping("/employees")
	public ResponseEntity<?> addNewEmployee(@RequestBody Employee newEmployee) throws URISyntaxException{
		// - If a method parameter is annotated with @RequestBody, 
		// Spring will bind the incoming HTTP request body
		// (for the URL mentioned in @RequestMapping for that method) to that parameter
		// - While doing that, Spring will [behind the scenes] use HTTP Message converters 
		// to convert the HTTP request body into domain object
		Resource<Employee> resource = employeeResourceAssembler.toResource(employeeRepository.save(newEmployee));
		// The new Employee object is saved as before. 
		// But the resulting object is wrapped using the EmployeeResourceAssembler.
		// Spring MVC’s ResponseEntity is used to create an HTTP 201 Created status message. 
		return ResponseEntity.created(
				new URI(resource.getId().expand().getHref()))
				.body(resource);
	}
	
	// - Overall, When we get HTTP GET request (via employee/employeeId url) this method will work and
	// returns correspond employee(which client wanted), then it will automatically convert the
	// appropriate JSON object with its links (thanks to Spring HATEOS)
	// NOTE THAT: produces is necessary otherwise get an error javax.xml.bind.JAXBException: class ... nor any of its super class is known to this context
	@RequestMapping(value = "/employees/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public Resource<Employee> getEmployee(@PathVariable Long id) {
		// @PathVariable is used to map the URI variable to one of the method arguments. 
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(String.valueOf(id)));
		// Now we added HATEOS to make our web service as a REST service.
		// Via HATEOS, our return doesn't include only data, also includes links
		return employeeResourceAssembler.toResource(employee);
		
		}
	
	// - Annotation for mapping HTTP PUT requests onto specific handler methods.
	// - Specifically, @PutMapping is a composed annotation that acts as a 
	// shortcut for @RequestMapping(method = RequestMethod.PUT).
	@PutMapping("/employees/{id}")
	public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {
		Employee updatedEmployee = employeeRepository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return employeeRepository.save(employee);
				})
				.orElseGet(() -> {
					newEmployee.setId(id);
					return employeeRepository.save(newEmployee);
				});
		Resource<Employee> resource = employeeResourceAssembler.toResource(updatedEmployee);
		
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}
	
	// Annotation for mapping HTTP DELETE requests onto specific handler methods.
	// Specifically, @DeleteMapping is a composed annotation that acts as a 
	// shortcut for @RequestMapping(method = RequestMethod.DELETE).
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		employeeRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
