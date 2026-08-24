package com.aslan.service.impl;

import com.aslan.dto.DtoEquipmentType;
import com.aslan.entity.EquipmentType;
import com.aslan.repository.EquipmentTypeRepository;
import com.aslan.service.EquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipmentTypeServiceImpl implements EquipmentTypeService {

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @Override
    public DtoEquipmentType createEquipmentType(DtoEquipmentType dto) {
        EquipmentType type = new EquipmentType();
        type.setName(dto.getName());
        return mapToDto(equipmentTypeRepository.save(type));
    }

    @Override
    public List<DtoEquipmentType> getAllEquipmentTypes() {
        return equipmentTypeRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DtoEquipmentType getEquipmentTypeById(Long id) {
        EquipmentType type = equipmentTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("EquipmentType not found"));
        return mapToDto(type);
    }

    @Override
    public DtoEquipmentType updateEquipmentType(Long id, DtoEquipmentType dto) {
        EquipmentType type = equipmentTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("EquipmentType not found"));
        type.setName(dto.getName());
        return mapToDto(equipmentTypeRepository.save(type));
    }

    @Override
    public void deleteEquipmentType(Long id) {
        equipmentTypeRepository.deleteById(id);
    }

    private DtoEquipmentType mapToDto(EquipmentType type) {
        DtoEquipmentType dto = new DtoEquipmentType();
        dto.setId(type.getId());
        dto.setName(type.getName());
        return dto;
    }
}
