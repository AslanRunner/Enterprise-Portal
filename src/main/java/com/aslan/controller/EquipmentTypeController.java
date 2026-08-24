package com.aslan.controller;

import com.aslan.dto.DtoEquipmentType;
import com.aslan.service.EquipmentTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment-types")
public class EquipmentTypeController {

    @Autowired
    private EquipmentTypeService equipmentTypeService;

    @PostMapping
    public ResponseEntity<DtoEquipmentType> createEquipmentType(@Valid @RequestBody DtoEquipmentType dto){
        DtoEquipmentType created = equipmentTypeService.createEquipmentType(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoEquipmentType>> getAllEquipmentTypes(){
        return ResponseEntity.ok(equipmentTypeService.getAllEquipmentTypes());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoEquipmentType> getEquipmentTypeById(@PathVariable Long id){
        return ResponseEntity.ok(equipmentTypeService.getEquipmentTypeById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoEquipmentType> updateEquipmentType(@PathVariable Long id,@Valid @RequestBody DtoEquipmentType dto){
        return ResponseEntity.ok(equipmentTypeService.updateEquipmentType(id,dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEquipmentType(@PathVariable Long id){
        equipmentTypeService.deleteEquipmentType(id);
        return ResponseEntity.noContent().build();
    }


}
