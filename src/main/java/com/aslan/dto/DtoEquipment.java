package com.aslan.dto;

import com.aslan.enums.EquipmentStatus;
import lombok.Data;

@Data
public class DtoEquipment {
    private Long id;
    private Long typeId;

    private String brand;
    private String model;
    private String serialNumber;

    private EquipmentStatus status;

}
