package com.aslan.service;

import com.aslan.dto.DtoRole;
import com.aslan.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleEntityById(Long id);
    DtoRole createRole(DtoRole requestDto);
    DtoRole getRoleById(Long id);
    List<DtoRole> getAllRoles();
    DtoRole updateRole(Long id, DtoRole requestDto);
    void deleteRole(Long id);
}
