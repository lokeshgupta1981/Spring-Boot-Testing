package com.howtodoinjava.employees.services;

import com.howtodoinjava.employees.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeService extends CrudRepository<Employee, Integer> {

}
