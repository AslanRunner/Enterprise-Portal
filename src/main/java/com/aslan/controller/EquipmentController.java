package com.aslan.controller;

import com.aslan.dto.DtoEquipment;
import com.aslan.enums.EquipmentStatus;
import com.aslan.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<DtoEquipment> createEquipment(@Valid @RequestBody DtoEquipment dto){
        DtoEquipment created = equipmentService.createEquipment(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoEquipment>> getAllEquipments(){
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @GetMapping("/get/status/{status}")
    public ResponseEntity<List<DtoEquipment>> getEquipmentsByStatus(@PathVariable EquipmentStatus status){
        return ResponseEntity.ok(equipmentService.getEquipmentsByStatus(status));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoEquipment> getEquipmentById(@PathVariable Long id){
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoEquipment> updateEquipment(@PathVariable Long id, @Valid @RequestBody DtoEquipment dto){
        return ResponseEntity.ok(equipmentService.updateEquipment(id,dto));
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<DtoEquipment> updateEquipmentStatus(@PathVariable Long id,@Valid @RequestBody EquipmentStatus status){
        return ResponseEntity.ok(equipmentService.updateEquipmentStatus(id,status));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id){
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

}
