package com.aslan.controller;

import com.aslan.dto.DtoSkill;
import com.aslan.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skill")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoSkill> getSkillById(@PathVariable Long id) {
        DtoSkill skill = skillService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoSkill>> getAllSkills() {
        List<DtoSkill> list = skillService.getAllSkills();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<DtoSkill> createSkill(@Valid @RequestBody DtoSkill requestDto) {
        DtoSkill skill = skillService.createSkill(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoSkill> updateSkill(@PathVariable Long id, @Valid @RequestBody DtoSkill requestDto) {
        return ResponseEntity.ok(skillService.updateSkill(id, requestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

}
