package com.aslan.controller;

import com.aslan.dto.DtoRoomReservationCreate;
import com.aslan.dto.DtoRoomReservationResponse;
import com.aslan.service.RoomReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-reservation")
public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;

    @PostMapping
    public ResponseEntity<DtoRoomReservationResponse> createReservation(@Valid @RequestBody DtoRoomReservationCreate dto){
        DtoRoomReservationResponse reservation = roomReservationService.createReservation(dto);
        return new ResponseEntity<>(reservation,HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DtoRoomReservationResponse> updateReservation(@PathVariable Long id, @Valid @RequestBody DtoRoomReservationCreate dto){
        DtoRoomReservationResponse reservation = roomReservationService.updateReservation(id, dto);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/get/personel/{personelId}")
    public ResponseEntity<List<DtoRoomReservationResponse>> getReservationByPersonelId(@PathVariable Long personelId){
        List<DtoRoomReservationResponse> responses = roomReservationService.getReservationByPersonelId(personelId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId){
        roomReservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/get/{reservationId}")
    public ResponseEntity<DtoRoomReservationResponse> getReservationById(@PathVariable Long reservationId) {
        return ResponseEntity.ok(roomReservationService.getReservationById(reservationId));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoRoomReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(roomReservationService.getAllReservations());
    }

    @GetMapping("/get/room/{roomId}")
    public ResponseEntity<List<DtoRoomReservationResponse>> getReservationsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomReservationService.getReservationsByRoomId(roomId));
    }

}
