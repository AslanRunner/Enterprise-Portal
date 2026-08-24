package com.aslan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DtoRoomReservationCreate {
    private Long roomId;
    private String name;
    private Long personelId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String purpose;
}
