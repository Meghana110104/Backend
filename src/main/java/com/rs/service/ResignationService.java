package com.rs.service;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.rs.dto.ResignationDTO;

public interface ResignationService {
    public ResponseEntity<String> applyResignation(Long id, ResignationDTO dto);

    public ResponseEntity<List<ResignationDTO>> getAll();
	public ResponseEntity<?> getById(Long id);

	 public ResponseEntity<String> deleteById(Long id);

	public ResponseEntity<String> updateById(Long id, ResignationDTO dto);

	
}
