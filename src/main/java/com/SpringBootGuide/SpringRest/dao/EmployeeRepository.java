package com.SpringBootGuide.SpringRest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBootGuide.SpringRest.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
