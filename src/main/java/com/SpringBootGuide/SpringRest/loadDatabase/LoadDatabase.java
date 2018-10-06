package com.SpringBootGuide.SpringRest.loadDatabase;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.SpringBootGuide.SpringRest.dao.EmployeeRepository;
import com.SpringBootGuide.SpringRest.entity.Employee;

import lombok.extern.slf4j.Slf4j;
/**
 * CommandLineRunner is a simple spring boot interface with a run method. 
 * The run method of all beans implementing the CommandLineRunner interface 
 * will be called automatically by the spring boot system after the initial boot.
 * for more information :
 * https://www.quickprogrammingtips.com/spring-boot/how-to-use-commandlinerunner-in-spring-boot-application.html
 * @author mehmetozanguven
 */
@Configuration
@Slf4j
public class LoadDatabase {
	
	/**
     * What happens when it gets loaded
     *  Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.
     *  This runner will request a copy of the EmployeeRepository we just created.
     *  Using it, it will create two entities and store them.
     * @param employeeRepository
     * @return 
     * @Slf4j is a Lombok annotation to autocreate an 
     *  Slf4j-based LoggerFactory as log, allowing us 
     *  to log these newly created "employees"
     */
	@Bean
	public CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
		return args -> {
			log.info("\nPreloading " + employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar")) + "\n");
			log.info("\nPreloading " + employeeRepository.save(new Employee("Frodo", "Baggins", "thief")) + "\n");
		};
	}
}
