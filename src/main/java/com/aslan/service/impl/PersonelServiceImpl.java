package com.aslan.service.impl;

import com.aslan.dto.*;
import com.aslan.entity.Department;
import com.aslan.entity.Personel;
import com.aslan.entity.Role;
import com.aslan.entity.Skill;
import com.aslan.exception.BadRequestException;
import com.aslan.exception.ResourceNotFoundException;

import com.aslan.repository.PersonelRepository;

import com.aslan.service.DepartmentService;
import com.aslan.service.PersonelService;
import com.aslan.service.RoleService;
import com.aslan.service.SkillService;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PersonelServiceImpl implements PersonelService {

    @Autowired
    private PersonelRepository personelRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DtoPersonelResponse createPersonel(DtoPersonelRequest requestDto) {
        Personel personel = new Personel();

        if (!StringUtils.hasText(requestDto.getPassword())) {
            throw new BadRequestException("Personel oluşturmak için şifre zorunludur.");
        }

        BeanUtils.copyProperties(requestDto, personel, "password");
        personel.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        personel.setActive(true);

        personel.setRegistrationNumber(new Random().nextLong(10000)); // Generate a random registration number

        Department department = departmentService.getDepartmentEntityById(requestDto.getDepartmentId());
        personel.setDepartment(department);

        Role role = roleService.getRoleEntityById(requestDto.getRoleId());
        personel.setRole(role);

        if (requestDto.getSkillIds() != null && !requestDto.getSkillIds().isEmpty()) {
            List<Skill> skills = skillService.getSkillEntitiesByIds(requestDto.getSkillIds());
            personel.setSkills(skills);
        }

        if (personelRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException(
                    "Bu e-posta sistemde zaten kayıtlı kayıtlı bir personele ait! Email: " + requestDto.getEmail());
        }

        Personel savedPersonel = personelRepository.save(personel);

        return convertToResponseDto(savedPersonel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoPersonelResponse> getAllPersonel() {
        List<Personel> personelList = personelRepository.findAll();

        return personelList.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DtoPersonelResponse getPersonelById(Long id) {
        Optional<Personel> optional = personelRepository.findById(id);

        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Personel bulunamadı! ID: " + id);
        }

        Personel personel = optional.get();

        return convertToResponseDto(personel);
    }

    @Override
    @Transactional
    public DtoPersonelResponse updatePersonel(Long id, DtoPersonelRequest requestDto) {
        Optional<Personel> optionalPersonel = personelRepository.findById(id);
        if (optionalPersonel.isEmpty()) {
            throw new ResourceNotFoundException("Güncellenmek istenen personel bulunamadı! ID: " + id);
        }
        Personel existPersonel = optionalPersonel.get();
        BeanUtils.copyProperties(requestDto, existPersonel, "password");

        if (StringUtils.hasText(requestDto.getPassword())) {
            existPersonel.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        Department department = departmentService.getDepartmentEntityById(requestDto.getDepartmentId());
        existPersonel.setDepartment(department);

        Role role = roleService.getRoleEntityById(requestDto.getRoleId());
        existPersonel.setRole(role);

        if (requestDto.getSkillIds() != null) {
            List<Skill> skills = skillService.getSkillEntitiesByIds(requestDto.getSkillIds());
            existPersonel.setSkills(skills);
        }

        if (personelRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new RuntimeException(
                    "Bu e-posta sistemde zaten kayıtlı kayıtlı bir personele ait! Email: " + requestDto.getEmail());
        }

        Personel updatedPersonel = personelRepository.save(existPersonel);
        return convertToResponseDto(updatedPersonel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoPersonelResponse> getActivePersonel() {
        return personelRepository.findByIsActiveTrue().stream().map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePersonel(Long id) {
        Optional<Personel> optional = personelRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Silinecek personel bulunamadı! ID: " + id);
        }

        personelRepository.delete(optional.get());
    }

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Personel personel = personelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personel bulunamadı! ID: " + id));

        if (!passwordEncoder.matches(oldPassword, personel.getPassword())) {
            throw new BadRequestException("Mevcut şifre hatalı!");
        }

        personel.setPassword(passwordEncoder.encode(newPassword));
        personelRepository.save(personel);
    }

    // Helper method
    public DtoPersonelResponse convertToResponseDto(Personel personel) {
        DtoPersonelResponse dto = new DtoPersonelResponse();

        BeanUtils.copyProperties(personel, dto);

        if (personel.getDepartment() != null) {
            dto.setDepartmentName(personel.getDepartment().getName());
        }

        if (personel.getRole() != null) {
            dto.setRoleName(personel.getRole().getName());
        }

        if (personel.getSkills() != null && !personel.getSkills().isEmpty()) {
            dto.setSkills(personel.getSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        }

        return dto;
    }
}
