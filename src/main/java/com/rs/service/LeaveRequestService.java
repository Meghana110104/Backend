package com.rs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.rs.dto.LeaveRequestDTO;

public interface LeaveRequestService {

public ResponseEntity<String> add(LeaveRequestDTO data);
public ResponseEntity<List<LeaveRequestDTO>> getAll();
public ResponseEntity<?> getById(Long id);
public ResponseEntity<String> deleteById(Long id);

public ResponseEntity<String> delete(Long id);
public ResponseEntity<?> updateLeave(long id, LeaveRequestDTO data);


}