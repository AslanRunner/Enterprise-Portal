package com.aslan.service;

import com.aslan.dto.DtoEquipmentAssignmentRequest;
import com.aslan.dto.DtoEquipmentAssignmentResponse;

import java.util.List;

public interface EquipmentAssignmentService {
    DtoEquipmentAssignmentResponse createRequest(DtoEquipmentAssignmentRequest request);
    DtoEquipmentAssignmentResponse assignEquipment(DtoEquipmentAssignmentRequest request);
    DtoEquipmentAssignmentResponse returnEquipment(Long assignmentId);
    List<DtoEquipmentAssignmentResponse> getAssignmentHistory(Long equipmentId);
    List<DtoEquipmentAssignmentResponse> getAssignmentsByPersonelId(Long personelId);
    List<DtoEquipmentAssignmentResponse> getActiveAssignments();
    List<DtoEquipmentAssignmentResponse> getAllAssignments();
    DtoEquipmentAssignmentResponse updateStatus(Long id, String status);
}
