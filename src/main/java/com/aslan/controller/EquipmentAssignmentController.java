package com.aslan.controller;

import com.aslan.dto.DtoEquipmentAssignmentRequest;
import com.aslan.dto.DtoEquipmentAssignmentResponse;
import com.aslan.service.EquipmentAssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment-assignments")
public class EquipmentAssignmentController {

    @Autowired
    private EquipmentAssignmentService equipmentAssignmentService;

    @PostMapping("/request")
    public ResponseEntity<DtoEquipmentAssignmentResponse> createRequest(@Valid @RequestBody DtoEquipmentAssignmentRequest request){
        DtoEquipmentAssignmentResponse response = equipmentAssignmentService.createRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<DtoEquipmentAssignmentResponse> assignEquipment(@Valid @RequestBody DtoEquipmentAssignmentRequest request){
        DtoEquipmentAssignmentResponse response = equipmentAssignmentService.assignEquipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/return/{assignmentId}")
    public ResponseEntity<DtoEquipmentAssignmentResponse> returnEquipment(@PathVariable Long assignmentId){
        return ResponseEntity.ok(equipmentAssignmentService.returnEquipment(assignmentId));
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<DtoEquipmentAssignmentResponse>> getAssignmentHistory(@PathVariable Long equipmentId){
        return ResponseEntity.ok(equipmentAssignmentService.getAssignmentHistory(equipmentId));
    }

    @GetMapping("/personel/{personelId}")
    public ResponseEntity<List<DtoEquipmentAssignmentResponse>> getAssignmentsByPersonelId(@PathVariable Long personelId){
        return ResponseEntity.ok(equipmentAssignmentService.getAssignmentsByPersonelId(personelId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DtoEquipmentAssignmentResponse>> getActiveAssignments(){
        return ResponseEntity.ok(equipmentAssignmentService.getActiveAssignments());
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoEquipmentAssignmentResponse>> getAllAssignments(){
        return ResponseEntity.ok(equipmentAssignmentService.getAllAssignments());
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<DtoEquipmentAssignmentResponse> updateStatus(@PathVariable Long id, @RequestParam String newStatus){
        return ResponseEntity.ok(equipmentAssignmentService.updateStatus(id, newStatus));
    }
}
