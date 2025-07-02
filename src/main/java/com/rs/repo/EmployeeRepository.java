package com.rs.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rs.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

	
	Optional<Employee> findByEmailId(String empId);
	Optional<Employee> findByMobileNo(String mobileNo);

	
}
