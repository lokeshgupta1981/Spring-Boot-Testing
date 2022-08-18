package com.howtodoinjava.employees;

import com.howtodoinjava.employees.controllers.EmployeeController;
import com.howtodoinjava.employees.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SystemTests {

	@Autowired
	private EmployeeController controller;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testCreateReadDelete() {
		String url = "http://localhost:"+port+"/employee";

		Employee employee = new Employee("Lokesh", "Gupta");
		ResponseEntity<Employee> entity = restTemplate.postForEntity(url, employee, Employee.class);

		Employee[] employees = restTemplate.getForObject(url, Employee[].class);
		Assertions.assertThat(employees).extracting(Employee::getFirstName).containsOnly("Lokesh");

		restTemplate.delete(url + "/" + entity.getBody().getId());
		Assertions.assertThat(restTemplate.getForObject(url, Employee[].class)).isEmpty();
	}

	@Test
	public void testErrorHandlingReturnsBadRequest() {

		String url = "http://localhost:"+port+"/wrong";

		try {
			restTemplate.getForEntity(url, String.class);
		} catch (HttpClientErrorException e) {
			Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		}
	}

}
