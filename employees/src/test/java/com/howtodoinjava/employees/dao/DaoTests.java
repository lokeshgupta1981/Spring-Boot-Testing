package com.howtodoinjava.employees.dao;


import com.howtodoinjava.employees.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
