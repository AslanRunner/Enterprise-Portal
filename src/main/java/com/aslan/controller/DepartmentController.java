package com.aslan.controller;

import com.aslan.dto.DtoDepartment;
import com.aslan.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DtoDepartment> createDepartment(@Valid @RequestBody DtoDepartment requestDto) {
        DtoDepartment createdDepartment = departmentService.createDepartment(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoDepartment> getDepartmentById(@PathVariable Long id) {
        DtoDepartment department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoDepartment>> getAllDepartments() {
        List<DtoDepartment> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoDepartment> updateDepartment(@PathVariable Long id, @RequestBody DtoDepartment request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

}
