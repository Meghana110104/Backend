package com.rs.service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rs.Entity.Employee;
import com.rs.Entity.Resignation;
import com.rs.dto.ResignationDTO;
import com.rs.exception.DuplicateFound;
import com.rs.exception.ResourceNotfound;
import com.rs.repo.EmployeeRepository;
import com.rs.repo.ResignationRepository;
@Service
public class ResignationServiceImplementation implements ResignationService {

    @Autowired
    private ResignationRepository resignationRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<String> applyResignation(Long id, ResignationDTO dto) {
        Optional<Employee> employee = employeeRepo.findById(id);
        if (employee.isEmpty()) {
            throw new ResourceNotfound("Employee not found with ID: " + id);
        }

        Optional<Resignation> existing = resignationRepo.findByEmployeeId(id);
        if (existing.isPresent()) {
            throw new DuplicateFound("Employee has already applied for resignation.");
        }

        Resignation resignation = new Resignation();
        resignation.setDateOfApplying(dto.getDateOfApplying());
        resignation.setReason(dto.getReason());
        resignation.setStatus(dto.getStatus());
        resignation.setEmployee(employee.get());

        resignationRepo.save(resignation);
        return ResponseEntity.ok("Resignation applied successfully.");
    }

    @Override
    public ResponseEntity<List<ResignationDTO>> getAll() {
        List<Resignation> data = resignationRepo.findAll();
        if (data.isEmpty()) {
            throw new ResourceNotfound("No resignation records found.");
        }

        List<ResignationDTO> dtoList = data.stream()
            .map(res -> modelMapper.map(res, ResignationDTO.class))
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
@Override
public ResponseEntity<?> getById(Long id){
	try {
		Optional<Resignation> data=resignationRepo.findById(id);
		if(data.isPresent()) {
		ResignationDTO dto=modelMapper.map(data.get(),ResignationDTO.class);
		return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID:"+id);
		}
		}
	catch(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:"+e.getMessage());
	}
	}
@Override
public ResponseEntity<String> deleteById(Long id) {
    Optional<Resignation> data = resignationRepo.findById(id);

    if (data.isPresent()) {
    	System.out.println("deleting : "+id);
        resignationRepo.deleteById(id);  
        return ResponseEntity.status(HttpStatus.OK).body("Resignation deleted with ID: " + id);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resignation not found with ID: " + id);
    }
}
@Override
public ResponseEntity<String> updateById(Long id, ResignationDTO dto) {
    Resignation existingData = resignationRepo.findById(id)
        .orElseThrow(() -> new ResourceNotfound("Resignation request with ID " + id + " not found."));

    String currentStatus = existingData.getStatus();
    if (!currentStatus.equalsIgnoreCase("request") && !currentStatus.equalsIgnoreCase("pending")) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Cannot update resignation with status: " + currentStatus);
    }

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate oldDate = LocalDate.parse(existingData.getDateOfApplying(), format);
    LocalDate newDate = LocalDate.parse(dto.getDateOfApplying(), format);

    if (!newDate.isAfter(oldDate)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("New date must be after existing date of applying.");
    }

    existingData.setDateOfApplying(dto.getDateOfApplying());
    
    // Optional: Allow status update if needed
    if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
        existingData.setStatus(dto.getStatus());
    }

    resignationRepo.save(existingData);
    return ResponseEntity.status(HttpStatus.OK).body("Resignation date updated successfully.");
}
}