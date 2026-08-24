package com.aslan.service.impl;

import com.aslan.dto.DtoEquipmentAssignmentRequest;
import com.aslan.dto.DtoEquipmentAssignmentResponse;
import com.aslan.entity.Equipment;
import com.aslan.entity.EquipmentAssignment;
import com.aslan.entity.EquipmentType;
import com.aslan.entity.Personel;
import com.aslan.enums.EquipmentStatus;
import com.aslan.exception.ResourceNotFoundException;
import com.aslan.repository.EquipmentAssignmentRepository;
import com.aslan.repository.EquipmentRepository;
import com.aslan.repository.PersonelRepository;
import com.aslan.service.EquipmentAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentAssignmentServiceImpl implements EquipmentAssignmentService {

    @Autowired
    private EquipmentAssignmentRepository assignmentRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PersonelRepository personelRepository;

    @Override
    @Transactional
    public DtoEquipmentAssignmentResponse createRequest(DtoEquipmentAssignmentRequest request) {
        Optional<Personel> optionalPersonel = personelRepository.findById(request.getPersonelId());
        if (optionalPersonel.isEmpty()) {
            throw new ResourceNotFoundException("Personel bulunamadı! ID: " + request.getPersonelId());
        }
        Personel personel = optionalPersonel.get();

        Optional<Equipment> optionalEquipment = equipmentRepository.findById(request.getEquipmentId());
        if (optionalEquipment.isEmpty()) {
            throw new ResourceNotFoundException("Ekipman bulunamadı! ID: " + request.getEquipmentId());
        }
        Equipment equipment = optionalEquipment.get();

        EquipmentAssignment assignment = new EquipmentAssignment();
        assignment.setPersonel(personel);
        assignment.setEquipment(equipment);
        if (equipment.getEquipmentType() != null) {
            assignment.setEquipmentType(equipment.getEquipmentType());
        }
        assignment.setPurpose(request.getPurpose());
        assignment.setStatus("BEKLIYOR");
        assignment.setDeliveryDate(LocalDate.now());

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional
    public DtoEquipmentAssignmentResponse assignEquipment(DtoEquipmentAssignmentRequest request) {

        Optional<Equipment> optional = equipmentRepository.findById(request.getEquipmentId());
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Alınacak ekipman bulunamadı! ID: " + request.getEquipmentId());
        }

        Equipment equipment = optional.get();

        Optional<Personel> optionalPersonel = personelRepository.findById(request.getPersonelId());
        if (optionalPersonel.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Ekipmanı almak isteyen kişi bulunamadı! ID: " + request.getPersonelId());
        }
        Personel personel = optionalPersonel.get();

        Optional<EquipmentAssignment> activeAssignment = assignmentRepository
                .findByEquipmentIdAndReturnDateIsNull(equipment.getId());
        if (activeAssignment.isPresent() || equipment.getStatus() == EquipmentStatus.PERSONELDE) {
            throw new RuntimeException("Bu ekipman zaten bir personele zimmetli!");
        }

        if (equipment.getStatus() == EquipmentStatus.ARIZALI) {
            throw new RuntimeException("Arızalı ekipman personele zimmetlenemez!");
        }

        EquipmentAssignment assignment = new EquipmentAssignment();
        assignment.setEquipment(equipment);
        assignment.setPersonel(personel);
        assignment.setDeliveryDate(request.getDeliveryDate() != null ? request.getDeliveryDate() : LocalDate.now());

        equipment.setStatus(EquipmentStatus.PERSONELDE);

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        equipmentRepository.save(equipment);

        return mapToResponseDto(saved);
    }

    @Override
    @Transactional
    public DtoEquipmentAssignmentResponse returnEquipment(Long assignmentId) {

        Optional<EquipmentAssignment> optional = assignmentRepository.findById(assignmentId);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("İade edilecek ekipman kaydı bulunamadı! ID: " + assignmentId);
        }
        EquipmentAssignment assignment = optional.get();

        if (assignment.getReturnDate() != null) {
            throw new RuntimeException("Bu zimmet kaydı daha önce iade edilmiş!");
        }

        assignment.setReturnDate(LocalDate.now());
        assignment.setStatus("İADE EDİLDİ");
        assignment.getEquipment().setStatus(EquipmentStatus.IADE_EDILDI);

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        equipmentRepository.save(assignment.getEquipment());

        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEquipmentAssignmentResponse> getAssignmentHistory(Long equipmentId) {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Zimmet geçmişi istenen ekipman bulunamadı! ID: " + equipmentId);
        }

        return assignmentRepository.findByEquipmentId(equipmentId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEquipmentAssignmentResponse> getAssignmentsByPersonelId(Long personelId) {
        if (!personelRepository.existsById(personelId)) {
            throw new ResourceNotFoundException("Zimmetleri istenen personel bulunamadı! ID: " + personelId);
        }

        return assignmentRepository.findByPersonelId(personelId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEquipmentAssignmentResponse> getActiveAssignments() {
        return assignmentRepository.findByReturnDateIsNull().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEquipmentAssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DtoEquipmentAssignmentResponse updateStatus(Long id, String status) {
        Optional<EquipmentAssignment> optional = assignmentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Zimmet kaydı bulunamadı! ID: " + id);
        }
        EquipmentAssignment assignment = optional.get();
        assignment.setStatus(status);

        if ("ONAYLANDI".equals(status)) {
            Equipment equipment = assignment.getEquipment();
            if (equipment != null) {
                equipment.setStatus(EquipmentStatus.PERSONELDE);
                equipmentRepository.save(equipment);
            }
        }

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        return mapToResponseDto(saved);
    }

    private DtoEquipmentAssignmentResponse mapToResponseDto(EquipmentAssignment assignment) {
        Equipment equipment = assignment.getEquipment();
        Personel personel = assignment.getPersonel();
        EquipmentType type = assignment.getEquipmentType();

        DtoEquipmentAssignmentResponse dto = new DtoEquipmentAssignmentResponse();
        dto.setId(assignment.getId());
        if (equipment != null) {
            dto.setEquipmentName(equipment.getBrand() + " " + equipment.getModel());
            dto.setSerialNumber(equipment.getSerialNumber());
        }
        if (type != null) {
            dto.setTypeName(type.getName());
        } else if (equipment != null && equipment.getEquipmentType() != null) {
            dto.setTypeName(equipment.getEquipmentType().getName());
        }

        dto.setPurpose(assignment.getPurpose());
        dto.setStatus(assignment.getStatus());
        dto.setDate(assignment.getDeliveryDate()); // for backward compatibility in frontend

        dto.setPersonelFullName(personel.getFirstName() + " " + personel.getLastName());
        dto.setDeliveryDate(assignment.getDeliveryDate());
        dto.setReturnDate(assignment.getReturnDate());
        return dto;
    }
}
