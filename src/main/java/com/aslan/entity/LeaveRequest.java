package com.aslan.entity;

import com.aslan.enums.LeaveStatus;
import com.aslan.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = true)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "request_days", nullable = false)
    private Integer requestDays;

    @Column(name = "manager_note", nullable = true, length = 1000)
    private String managerNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", nullable = false)
    private LeaveStatus leaveStatus = LeaveStatus.ONAY_BEKLIYOR; // Default status for newly created leave requests

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch: relationship is resolved only when accessed (not at load time)
    @JoinColumn(name = "personel_id", nullable = false)
    private Personel personel;

}
