package com.aslan.service;

import com.aslan.dto.DtoDepartment;
import com.aslan.entity.Department;

import java.util.List;

public interface DepartmentService {


    DtoDepartment createDepartment(DtoDepartment requestDto);
    Department getDepartmentEntityById(Long id);
    DtoDepartment getDepartmentById(Long id);
    List<DtoDepartment> getAllDepartments();
    DtoDepartment updateDepartment(Long id, DtoDepartment request);
    void deleteDepartment(Long id);
}