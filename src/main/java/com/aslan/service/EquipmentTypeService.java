package com.aslan.service;

import com.aslan.dto.DtoEquipmentType;

import java.util.List;

public interface EquipmentTypeService {
    DtoEquipmentType createEquipmentType(DtoEquipmentType dto);
    List<DtoEquipmentType> getAllEquipmentTypes();
    DtoEquipmentType getEquipmentTypeById(Long id);
    DtoEquipmentType updateEquipmentType(Long id, DtoEquipmentType dto);
    void deleteEquipmentType(Long id);
}
