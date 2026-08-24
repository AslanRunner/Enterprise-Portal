package com.aslan.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DtoEquipmentAssignmentRequest {
    private Long equipmentId;
    private Long typeId;
    private Long personelId;
    private LocalDate deliveryDate;
    private String purpose;
}
