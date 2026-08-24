package com.aslan.controller;

import com.aslan.dto.DtoAnnouncementRequest;
import com.aslan.dto.DtoAnnouncementResponse;
import com.aslan.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/announcement")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<DtoAnnouncementResponse> createAnnouncement(
            @Valid @RequestBody DtoAnnouncementRequest request) {
        DtoAnnouncementResponse dtoRequest = announcementService.createAnnouncement(request);
        return new ResponseEntity<>(dtoRequest, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoAnnouncementResponse>> getAllAnnouncement() {
        List<DtoAnnouncementResponse> responses = announcementService.getAllAnnouncement();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/like/{announcementId}")
    public ResponseEntity<Void> likeAnnouncement(@PathVariable Long announcementId) {
        announcementService.likeAnnouncement(announcementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{announcementId}")
    public ResponseEntity<DtoAnnouncementResponse> getAnnouncementById(@PathVariable Long announcementId) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(announcementId));

    }

    @PutMapping("/update/{announcementId}")
    public ResponseEntity<DtoAnnouncementResponse> updateAnnouncement(@PathVariable Long announcementId,
            @Valid @RequestBody DtoAnnouncementRequest request) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(announcementId, request));
    }

    @DeleteMapping("/delete/{announcementId}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long announcementId) {
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.noContent().build();
    }

}
