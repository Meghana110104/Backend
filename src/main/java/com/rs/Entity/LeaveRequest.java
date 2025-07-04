package com.rs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "leaverequest")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startDate;
    private String endDate;
    private String reason;
    private String status;
   
    // Relationship to Employee
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")  
    @JsonBackReference
    private Employee employee;
}
