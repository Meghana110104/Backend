package com.rs.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
public class EmployeeDTO {
	private Long id;
	private String fullName;
	private String mobileNo;
	private String emailId;
	private String desigation;
	private String empId;
	private String role;
	private String status;
	
	@JsonManagedReference
	private List<LeaveRequestDTO> leaveRequests; // Optional back-reference
	@JsonManagedReference
	private List<ResignationDTO> resignation;
}
