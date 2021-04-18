package com.howtodoinjava.employees.dao;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.howtodoinjava.employees.model.Employee;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DaoTests {

  @Autowired
  EmployeeRepository employeeRepository;

  @Test
  public void testCreateReadDelete() {
    Employee employee = new Employee("Lokesh", "Gupta");

    employeeRepository.save(employee);

    Iterable<Employee> employees = employeeRepository.findAll();
    Assertions.assertThat(employees).extracting(Employee::getFirstName).containsOnly("Lokesh");

    employeeRepository.deleteAll();
    Assertions.assertThat(employeeRepository.findAll()).isEmpty();
  }
}
