package com.aslan.service.impl;

import com.aslan.dto.DtoEquipment;
import com.aslan.entity.Equipment;
import com.aslan.entity.EquipmentType;
import com.aslan.enums.EquipmentStatus;
import com.aslan.exception.ResourceNotFoundException;
import com.aslan.repository.EquipmentRepository;
import com.aslan.repository.EquipmentTypeRepository;
import com.aslan.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @Override
    @Transactional
    public DtoEquipment createEquipment(DtoEquipment dto) {
        EquipmentType type = equipmentTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Sistemde bu ID'ye sahip bir ekipman tipi bulunamadı! ID: " + dto.getTypeId()));

        if (equipmentRepository.existsBySerialNumber(dto.getSerialNumber())){
            throw new RuntimeException("Bu seri numarasıyla kayıtlı bir ekipman zaten mevcut! Seri No: " + dto.getSerialNumber());
        }

        Equipment equipment = new Equipment();
        equipment.setBrand(dto.getBrand());
        equipment.setModel(dto.getModel());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setEquipmentType(type);
        equipment.setStatus(EquipmentStatus.DEPODA);

        Equipment saved = equipmentRepository.save(equipment);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEquipment> getAllEquipments() {
        return equipmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEquipment> getEquipmentsByStatus(EquipmentStatus status) {
        return equipmentRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DtoEquipment getEquipmentById(Long id) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Silinecek ekipman bulunamadı! ID: "+id);
        }
        Equipment equipment = optional.get();

        return mapToDto(equipment);
    }

    @Override
    @Transactional
    public DtoEquipment updateEquipment(Long id, DtoEquipment dto) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Güllenecek ekipman bulunamadı! ID: "+id);
        }

        Optional<EquipmentType> optionalType = equipmentTypeRepository.findById(dto.getTypeId());
            if (optionalType.isEmpty()){
            throw new ResourceNotFoundException("Ekipman tipi bulunamadı! ID: "+ dto.getTypeId());
        }

        EquipmentType equipmentType = optionalType.get();

        Equipment equipment = optional.get();
        equipment.setModel(dto.getModel());
        equipment.setBrand(dto.getBrand());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setEquipmentType(equipmentType);

        Equipment update = equipmentRepository.save(equipment);
        return mapToDto(update);
    }

    @Override
    @Transactional
    public DtoEquipment updateEquipmentStatus(Long id, EquipmentStatus status) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Statüsü güllenecek ekipman bulunamadı! ID: "+id);
        }
        Equipment equipment = optional.get();
        equipment.setStatus(status);
        Equipment update = equipmentRepository.save(equipment);
        return mapToDto(update);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long id) {
        Optional<Equipment> optional = equipmentRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Silinecek ekipman bulunamadı! ID: "+id);
        }
        Equipment equipment = optional.get();
        equipmentRepository.delete(equipment);
    }

    private DtoEquipment mapToDto(Equipment equipment){
        DtoEquipment dto = new DtoEquipment();
        dto.setId(equipment.getId());
        dto.setTypeId(equipment.getEquipmentType().getId());
        dto.setBrand(equipment.getBrand());
        dto.setModel(equipment.getModel());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setStatus(equipment.getStatus());
        return dto;
    }
}
