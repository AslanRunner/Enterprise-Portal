package com.aslan.dto;

import com.aslan.enums.LeaveStatus;
import com.aslan.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DtoLeaveRequestResponse {
    private Long id; // Leave request identifier
    private Long personelId;

    private String firstName;
    private String lastName;

    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer requestedDays;
    private String managerNote;
    private LeaveStatus leaveStatus;
}
