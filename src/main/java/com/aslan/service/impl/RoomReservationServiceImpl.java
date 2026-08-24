package com.aslan.service.impl;

import com.aslan.dto.DtoRoomReservationCreate;
import com.aslan.dto.DtoRoomReservationResponse;
import com.aslan.entity.MeetingRoom;
import com.aslan.entity.Personel;
import com.aslan.entity.RoomReservation;
import com.aslan.repository.MeetingRoomRepository;
import com.aslan.repository.PersonelRepository;
import com.aslan.repository.RoomReservationRepository;
import com.aslan.service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.aslan.exception.BadRequestException;

@Service
@Transactional
public class RoomReservationServiceImpl implements RoomReservationService {

    @Autowired
    private RoomReservationRepository roomReservationRepository;
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private PersonelRepository personelRepository;

    @Override
    public DtoRoomReservationResponse createReservation(DtoRoomReservationCreate dto) {
        Personel personel = personelRepository.findById(dto.getPersonelId()).orElseThrow(() -> new RuntimeException("Personel not found"));
        MeetingRoom room = meetingRoomRepository.findById(dto.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));

        if (dto.getStartTime().getHour() < 8 || dto.getEndTime().getHour() > 18 || 
            (dto.getEndTime().getHour() == 18 && dto.getEndTime().getMinute() > 0)) {
            throw new BadRequestException("Rezervasyonlar sadece 08:00 ile 18:00 saatleri arasında yapılabilir.");
        }

        List<RoomReservation> existingReservations = roomReservationRepository.findAll().stream()
                .filter(r -> r.getMeetingRoom().getId().equals(dto.getRoomId()))
                .collect(Collectors.toList());

        for (RoomReservation r : existingReservations) {
            if (dto.getStartTime().isBefore(r.getEndTime()) && dto.getEndTime().isAfter(r.getStartTime())) {
                throw new BadRequestException("Seçilen saatlerde bu oda için başka bir rezervasyon bulunmaktadır.");
            }
        }
        
        RoomReservation reservation = new RoomReservation();
        reservation.setName(dto.getName());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setPurpose(dto.getPurpose());
        reservation.setPersonel(personel);
        reservation.setMeetingRoom(room);
        
        return mapToDto(roomReservationRepository.save(reservation));
    }

    @Override
    public DtoRoomReservationResponse updateReservation(Long id, DtoRoomReservationCreate dto) {
        RoomReservation reservation = roomReservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
                
        Personel personel = personelRepository.findById(dto.getPersonelId())
                .orElseThrow(() -> new RuntimeException("Personel not found"));
        MeetingRoom room = meetingRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (dto.getStartTime().getHour() < 8 || dto.getEndTime().getHour() > 18 || 
            (dto.getEndTime().getHour() == 18 && dto.getEndTime().getMinute() > 0)) {
            throw new BadRequestException("Rezervasyonlar sadece 08:00 ile 18:00 saatleri arasında yapılabilir.");
        }

        List<RoomReservation> existingReservations = roomReservationRepository.findAll().stream()
                .filter(r -> r.getMeetingRoom().getId().equals(dto.getRoomId()) && !r.getId().equals(id))
                .collect(Collectors.toList());

        for (RoomReservation r : existingReservations) {
            if (dto.getStartTime().isBefore(r.getEndTime()) && dto.getEndTime().isAfter(r.getStartTime())) {
                throw new BadRequestException("Seçilen saatlerde bu oda için başka bir rezervasyon bulunmaktadır.");
            }
        }
        
        reservation.setName(dto.getName());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setPurpose(dto.getPurpose());
        reservation.setPersonel(personel);
        reservation.setMeetingRoom(room);
        
        return mapToDto(roomReservationRepository.save(reservation));
    }

    @Override
    public List<DtoRoomReservationResponse> getReservationByPersonelId(Long personelId) {
        return roomReservationRepository.findAll().stream()
                .filter(r -> r.getPersonel().getId().equals(personelId))
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteReservation(Long reservationId) {
        roomReservationRepository.deleteById(reservationId);
    }

    @Override
    public DtoRoomReservationResponse getReservationById(Long reservationId) {
        return mapToDto(roomReservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Override
    public List<DtoRoomReservationResponse> getAllReservations() {
        return roomReservationRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<DtoRoomReservationResponse> getReservationsByRoomId(Long roomId) {
        return roomReservationRepository.findAll().stream()
                .filter(r -> r.getMeetingRoom().getId().equals(roomId))
                .map(this::mapToDto).collect(Collectors.toList());
    }

    private DtoRoomReservationResponse mapToDto(RoomReservation reservation) {
        DtoRoomReservationResponse dto = new DtoRoomReservationResponse();
        dto.setId(reservation.getId());
        dto.setName(reservation.getName());
        
        if (reservation.getMeetingRoom() != null) {
            dto.setRoomId(reservation.getMeetingRoom().getId());
            dto.setRoomName(reservation.getMeetingRoom().getName());
        }
        
        if (reservation.getPersonel() != null) {
            dto.setPersonelId(reservation.getPersonel().getId());
            dto.setFirstName(reservation.getPersonel().getFirstName());
            dto.setLastName(reservation.getPersonel().getLastName());
        }
        
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setPurpose(reservation.getPurpose());
        return dto;
    }
}
