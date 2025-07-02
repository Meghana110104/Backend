package com.rs.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
public class LeaveRequestDTO {
	private Long id;
	private String startDate;
	private String endDate;
	private String reason;
	private String mobileNo;
	private String status;

	@JsonBackReference
	private EmployeeDTO employee; // Reference to parent employee
}
