package com.rs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rs.dto.EmployeeDTO;
public interface EmployeeService {
public ResponseEntity<String> add(EmployeeDTO data);

public ResponseEntity<List<EmployeeDTO>> getAll();
public ResponseEntity<?> getById(Long id);

public ResponseEntity<String> deleteById(Long id);
public ResponseEntity<?> updateById(long id, EmployeeDTO data);

}