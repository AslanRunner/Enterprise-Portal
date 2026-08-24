package com.aslan.service.impl;

import com.aslan.dto.DtoRole;
import com.aslan.entity.Role;
import com.aslan.repository.RoleRepository;
import com.aslan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleEntityById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public DtoRole getRoleById(Long id) {
        return mapToDto(getRoleEntityById(id));
    }

    @Override
    public List<DtoRole> getAllRoles() {
        return roleRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DtoRole createRole(DtoRole requestDto) {
        Role role = new Role();
        role.setName(requestDto.getName());
        return mapToDto(roleRepository.save(role));
    }

    @Override
    public DtoRole updateRole(Long id, DtoRole requestDto) {
        Role role = getRoleEntityById(id);
        role.setName(requestDto.getName());
        return mapToDto(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    private DtoRole mapToDto(Role role) {
        DtoRole dto = new DtoRole();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}
