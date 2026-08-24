package com.aslan.service;

import com.aslan.dto.DtoSkill;
import com.aslan.entity.Skill;

import java.util.List;

public interface SkillService {
    Skill getSkillEntityById(Long id);
    List<Skill> getSkillEntitiesByIds(List<Long> ids);

    DtoSkill getSkillById(Long id);
    List<DtoSkill> getAllSkills();
    DtoSkill createSkill(DtoSkill requestDto);
    DtoSkill updateSkill(Long id, DtoSkill requestDto);
    void deleteSkill(Long id);
}
