package com.aslan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DtoRoomReservationResponse {
    private Long id;
    private String name;

    private Long roomId;
    private String roomName;


    private Long personelId;
    private String firstName;
    private String lastName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
}
