package com.aslan.dto;

import com.aslan.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DtoLeaveRequestCreate {

    private Long personelId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String managerNote;

}
