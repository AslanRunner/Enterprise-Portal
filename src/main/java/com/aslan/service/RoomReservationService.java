package com.aslan.service;

import com.aslan.dto.DtoRoomReservationCreate;
import com.aslan.dto.DtoRoomReservationResponse;

import java.util.List;

public interface RoomReservationService {
    DtoRoomReservationResponse createReservation(DtoRoomReservationCreate dto);
    DtoRoomReservationResponse updateReservation(Long id, DtoRoomReservationCreate dto);
    List<DtoRoomReservationResponse> getReservationByPersonelId(Long personelId);
    void deleteReservation(Long reservationId);
    DtoRoomReservationResponse getReservationById(Long reservationId);
    List<DtoRoomReservationResponse> getAllReservations();
    List<DtoRoomReservationResponse> getReservationsByRoomId(Long roomId);
}
