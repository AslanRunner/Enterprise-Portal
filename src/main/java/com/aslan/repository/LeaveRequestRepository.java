package com.aslan.repository;

import com.aslan.entity.LeaveRequest;
import com.aslan.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByPersonelId(Long personelId);
    List<LeaveRequest> findByLeaveStatus(LeaveStatus leaveStatus);
}
