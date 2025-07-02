package com.rs.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rs.Entity.Employee;
import com.rs.dto.EmployeeDTO;
import com.rs.exception.DuplicateFound;
import com.rs.exception.ResourceNotfound;
import com.rs.repo.EmployeeRepository;

@Service
public class EmployeeServiceImplementation implements EmployeeService {
@Autowired
private EmployeeRepository repo;
@Autowired
private ModelMapper modelmapper;
public ResponseEntity<String> add(EmployeeDTO data){
	
	Optional<Employee> existingEmailId = repo.findByEmailId(data.getEmailId());
	Optional<Employee> existingMobileNo= repo.findByMobileNo(data.getMobileNo());
	
	if(existingEmailId.isPresent() || existingMobileNo.isPresent()) {
		throw new DuplicateFound("Data Already exits");
	}
	repo.save(modelmapper.map(data, Employee.class));
	
	return
			ResponseEntity.status(HttpStatus.CREATED).body("Data Inserted Successfully");
}
@Override
public ResponseEntity<List<EmployeeDTO>> getAll() {
    List<Employee> data = repo.findAll();
    if (data.isEmpty()) {
        throw new ResourceNotfound("Employee records not found");
    }
    List<EmployeeDTO> dtoData = data.stream()
        .map(d -> modelmapper.map(d, EmployeeDTO.class))
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtoData);
}
@Override
    public ResponseEntity<?> getById(Long id) {
        try {
            Optional<Employee> data = repo.findById(id);

            if (data.isPresent()) {
                EmployeeDTO dto = modelmapper.map(data.get(), EmployeeDTO.class);
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

   @Override
    public ResponseEntity<String> deleteById(Long id) {
        try {
            Optional<Employee> data = repo.findById(id);

            if (data.isPresent()) {
                repo.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Employee no content with ID: " + id);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
   @Override
   public ResponseEntity<String> updateById(long id, EmployeeDTO data) {
	    Optional<Employee> existingData = repo.findById(id);

	    if (!existingData.isPresent()) {
	        throw new ResourceNotfound("Employee with ID " + id + " not found.");
	    }else {

	    Employee existingUser = existingData.get();
	    // for this deatils we do'tneed to check
	    existingUser.setDesigation(data.getDesigation());
        existingUser.setFullName(data.getFullName());
        
	    // Dto Details
	    String newMobileNo = data.getMobileNo();
	    String newEmailId = data.getEmailId();
	    // database details 
	    String mobNo = existingUser.getMobileNo();
	    String emailId = existingUser.getEmailId();
	   
	    // checking duplicate for mobile no
	    if(newMobileNo.equalsIgnoreCase(mobNo)) {
	    	existingUser.setMobileNo(newMobileNo);
	    }else {
	    	Optional<Employee> duplicateDataforMobileNo = repo.findByMobileNo(newMobileNo);
	    	if(duplicateDataforMobileNo.isPresent()) {
	    		throw new DuplicateFound("Duplicate found for Mobile No");
	    	}else {
	    		existingUser.setMobileNo(newMobileNo);
	    	}
	    }
	    
	    // checking duplicate for Email id
	    if(newEmailId.equalsIgnoreCase(emailId)) {
	    	existingUser.setEmailId(newEmailId);
	    }else {
	    	Optional<Employee> duplicateDataforEmailId = repo.findByEmailId(newEmailId);
	    	if(duplicateDataforEmailId.isPresent()) {
	    		throw new DuplicateFound("Duplicate found for Email id");
	    	}else {
	    		existingUser.setEmailId(newEmailId);
	    	}
	    }

	    repo.save(existingUser);

	    return ResponseEntity.status(HttpStatus.OK).body("Data was updated.");
	    }
   }
}
