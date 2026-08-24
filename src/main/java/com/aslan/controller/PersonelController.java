package com.aslan.controller;

import com.aslan.dto.DtoPersonelRequest;
import com.aslan.dto.DtoPersonelResponse;
import com.aslan.service.PersonelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personel")
public class PersonelController {

    @Autowired
    private PersonelService personelService;

    @PostMapping
    public ResponseEntity<DtoPersonelResponse> createPersonel(@Valid @RequestBody DtoPersonelRequest request) {
        DtoPersonelResponse response = personelService.createPersonel(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoPersonelResponse>> getAllPersonel() {
        return ResponseEntity.ok(personelService.getAllPersonel());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoPersonelResponse> getPersonelById(@PathVariable Long id) {
        return ResponseEntity.ok(personelService.getPersonelById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoPersonelResponse> updatePersonel(@PathVariable Long id, @Valid @RequestBody DtoPersonelRequest personelDetails){
        return ResponseEntity.ok(personelService.updatePersonel(id, personelDetails));
    }

    @GetMapping("/get/active-personel")
    public ResponseEntity<List<DtoPersonelResponse>> getActivePersonel(){
        return ResponseEntity.ok(personelService.getActivePersonel());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePersonel(@PathVariable Long id){
        personelService.deletePersonel(id);

        return ResponseEntity.noContent().build(); // 204 No Content — successful deletion with empty body
    }
    @PutMapping("/change-password/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody java.util.Map<String, String> payload) {
        personelService.changePassword(id, payload.get("oldPassword"), payload.get("newPassword"));
        return ResponseEntity.ok().build();
    }
}