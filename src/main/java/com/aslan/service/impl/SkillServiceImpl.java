package com.aslan.service.impl;

import com.aslan.dto.DtoSkill;
import com.aslan.entity.Skill;
import com.aslan.repository.SkillRepository;
import com.aslan.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public Skill getSkillEntityById(Long id) {
        return skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    @Override
    public List<Skill> getSkillEntitiesByIds(List<Long> ids) {
        return skillRepository.findAllById(ids);
    }

    @Override
    public DtoSkill getSkillById(Long id) {
        return mapToDto(getSkillEntityById(id));
    }

    @Override
    public List<DtoSkill> getAllSkills() {
        return skillRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DtoSkill createSkill(DtoSkill requestDto) {
        Skill skill = new Skill();
        skill.setName(requestDto.getName());
        return mapToDto(skillRepository.save(skill));
    }

    @Override
    public DtoSkill updateSkill(Long id, DtoSkill requestDto) {
        Skill skill = getSkillEntityById(id);
        skill.setName(requestDto.getName());
        return mapToDto(skillRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    private DtoSkill mapToDto(Skill skill) {
        DtoSkill dto = new DtoSkill();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        return dto;
    }
}
