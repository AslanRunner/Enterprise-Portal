package com.aslan.controller;

import com.aslan.dto.DtoRole;
import com.aslan.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<DtoRole> createRole(@Valid @RequestBody DtoRole requestDto){
        DtoRole role = roleService.createRole(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoRole> getRoleById(@PathVariable Long id){
        DtoRole role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoRole>> getAllRoles(){
        List<DtoRole> list = roleService.getAllRoles();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoRole> updateRole(@PathVariable Long id,@Valid @RequestBody DtoRole requestDto){
        return ResponseEntity.ok(roleService.updateRole(id,requestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }



}
