package com.aslan.service.impl;

import com.aslan.dto.DtoMeetingRoom;
import com.aslan.entity.MeetingRoom;
import com.aslan.exception.ResourceNotFoundException;
import com.aslan.repository.MeetingRoomRepository;
import com.aslan.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Override
    public DtoMeetingRoom createMeetingRoom(DtoMeetingRoom dto) {
        if (meetingRoomRepository.existsByName(dto.getName())){
            throw new RuntimeException("Bu isimde bir toplantı odası zaten mevcut! Name: " + dto.getName());
        }
        MeetingRoom room = new MeetingRoom();
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());

        MeetingRoom saved = meetingRoomRepository.save(room);
        return mapToDto(saved);
    }

    @Override
    public List<DtoMeetingRoom> getAllMeetingRooms() {
        return meetingRoomRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DtoMeetingRoom getMeetingRoomById(Long id) {
        Optional<MeetingRoom> optional = meetingRoomRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Toplantı odası bulunamadı! ID: "+id);
        }
        MeetingRoom meetingRoom = optional.get();

        return mapToDto(meetingRoom);
    }

    @Override
    public DtoMeetingRoom updateMeetingRoom(Long id, DtoMeetingRoom dto) {
        Optional<MeetingRoom> optional = meetingRoomRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Toplantı odası bulunamadı! ID: "+id);
        }
        MeetingRoom meetingRoom = optional.get();
        meetingRoom.setName(dto.getName());
        meetingRoom.setCapacity(dto.getCapacity());

        MeetingRoom update = meetingRoomRepository.save(meetingRoom);

        return mapToDto(update);
    }

    @Override
    public void deleteMeetingRoom(Long id) {
        Optional<MeetingRoom> optional = meetingRoomRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("Silinecek toplantı odası bulunamadı! ID: "+ id);
        }
        MeetingRoom meetingRoom = optional.get();
        meetingRoomRepository.delete(meetingRoom);
    }

    private DtoMeetingRoom mapToDto(MeetingRoom room){
        DtoMeetingRoom dto = new DtoMeetingRoom();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());

        return dto;
    }
}
