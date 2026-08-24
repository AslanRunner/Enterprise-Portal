package com.aslan.controller;

import com.aslan.dto.DtoMeetingRoom;
import com.aslan.service.MeetingRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting-rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @PostMapping
    public ResponseEntity<DtoMeetingRoom> createMeetingRoom(@Valid @RequestBody DtoMeetingRoom dto) {
        DtoMeetingRoom room = meetingRoomService.createMeetingRoom(dto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoMeetingRoom>> getAllMeetingRooms() {
        List<DtoMeetingRoom> rooms = meetingRoomService.getAllMeetingRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DtoMeetingRoom> getMeetingRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingRoomService.getMeetingRoomById(id));
    }

    @PutMapping("/updated/{id}")
    public ResponseEntity<DtoMeetingRoom> updateMeetingRoom(@PathVariable Long id,
            @Valid @RequestBody DtoMeetingRoom dto) {
        return ResponseEntity.ok(meetingRoomService.updateMeetingRoom(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable Long id) {
        meetingRoomService.deleteMeetingRoom(id);
        return ResponseEntity.noContent().build();
    }

}
