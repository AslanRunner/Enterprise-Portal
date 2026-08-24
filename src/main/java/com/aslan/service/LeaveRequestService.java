package com.aslan.service;


import com.aslan.dto.DtoLeaveRequestCreate;
import com.aslan.dto.DtoLeaveRequestResponse;
import com.aslan.enums.LeaveStatus;

import java.util.List;

public interface LeaveRequestService {
    DtoLeaveRequestResponse createLeaveRequest(DtoLeaveRequestCreate dto);
    List<DtoLeaveRequestResponse> getLeaveRequestsByPersonelId(Long personelId);
    DtoLeaveRequestResponse updateLeaveStatus(Long leaveId, LeaveStatus newStatus, String managerNote);
    void deleteLeaveRequest(Long leaveId);
    DtoLeaveRequestResponse getLeaveRequestById(Long leaveId);
    List<DtoLeaveRequestResponse> getAllLeaveRequests();
    List<DtoLeaveRequestResponse> getLeaveRequestsByStatus(LeaveStatus status);

}
