package com.howtodoinjava.employees;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.howtodoinjava.employees.controllers.EmployeeController;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeesApplicationTests {

	@Autowired
	EmployeeController employeeController;

	@Test
	public void contextLoads() {
		Assertions.assertThat(employeeController).isNotNull();
	}
}
