package com.penacony.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.penacony.hotel.entity.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployeeId(String employeeId);
}