package com.aslan.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DtoEquipmentAssignmentResponse {
    private Long id;

    private String equipmentName;
    private String serialNumber;
    private String typeName;
    private String purpose;
    private String status;

    private String personelFullName;

    private LocalDate deliveryDate;
    private LocalDate returnDate;
    private LocalDate date;
}
