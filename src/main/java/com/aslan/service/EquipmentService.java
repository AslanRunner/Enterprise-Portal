package com.aslan.service;

import com.aslan.dto.DtoEquipment;
import com.aslan.enums.EquipmentStatus;

import java.util.List;

public interface EquipmentService {
    DtoEquipment createEquipment(DtoEquipment dto);
    List<DtoEquipment> getAllEquipments();
    List<DtoEquipment> getEquipmentsByStatus(EquipmentStatus status);
    DtoEquipment getEquipmentById(Long id);
    DtoEquipment updateEquipment(Long id, DtoEquipment dto);
    DtoEquipment updateEquipmentStatus(Long id, EquipmentStatus status);
    void deleteEquipment(Long id);
}
