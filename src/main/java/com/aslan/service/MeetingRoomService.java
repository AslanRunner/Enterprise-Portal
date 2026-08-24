package com.aslan.service;

import com.aslan.dto.DtoMeetingRoom;

import java.util.List;

public interface MeetingRoomService {
    DtoMeetingRoom createMeetingRoom(DtoMeetingRoom dto);
    List<DtoMeetingRoom> getAllMeetingRooms();
    DtoMeetingRoom getMeetingRoomById(Long id);
    DtoMeetingRoom updateMeetingRoom(Long id, DtoMeetingRoom dto);
    void deleteMeetingRoom(Long id);
}
