package com.aslan.service.impl;

import com.aslan.dto.DtoDepartment;
import com.aslan.entity.Department;
import com.aslan.repository.DepartmentRepository;
import com.aslan.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DtoDepartment createDepartment(DtoDepartment requestDto) {
        Department department = new Department();
        department.setName(requestDto.getName());
        return mapToDto(departmentRepository.save(department));
    }

    @Override
    public Department getDepartmentEntityById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public DtoDepartment getDepartmentById(Long id) {
        return mapToDto(getDepartmentEntityById(id));
    }

    @Override
    public List<DtoDepartment> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DtoDepartment updateDepartment(Long id, DtoDepartment request) {
        Department department = getDepartmentEntityById(id);
        department.setName(request.getName());
        return mapToDto(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private DtoDepartment mapToDto(Department department) {
        DtoDepartment dto = new DtoDepartment();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }
}
