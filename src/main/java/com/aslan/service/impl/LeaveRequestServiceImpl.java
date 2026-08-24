package com.aslan.service.impl;

import com.aslan.dto.DtoLeaveRequestCreate;
import com.aslan.dto.DtoLeaveRequestResponse;
import com.aslan.entity.LeaveRequest;
import com.aslan.entity.Personel;
import com.aslan.enums.LeaveStatus;
import com.aslan.exception.ResourceNotFoundException;
import com.aslan.repository.LeaveRequestRepository;
import com.aslan.repository.PersonelRepository;
import com.aslan.service.LeaveRequestService;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private PersonelRepository personelRepository;

    @Override
    @Transactional
    public DtoLeaveRequestResponse createLeaveRequest(DtoLeaveRequestCreate dto) {

        Optional<Personel> optional = personelRepository.findById(dto.getPersonelId());
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Personel bulunamadı! ID: " + dto.getPersonelId());
        }
        Personel personel = optional.get();

        int requestedDays = calculateDays(dto.getStartDate(), dto.getEndDate());
        if (requestedDays <= 0) {
            throw new RuntimeException("Invalid date range! Leave request must be at least 1 day."); // Consider using a custom exception
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setPersonel(personel);
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setRequestDays(requestedDays);
        leaveRequest.setManagerNote(dto.getManagerNote());
        leaveRequest.setLeaveStatus(LeaveStatus.ONAY_BEKLIYOR);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoLeaveRequestResponse> getLeaveRequestsByPersonelId(Long personelId) {
        List<LeaveRequest> requests = leaveRequestRepository.findByPersonelId(personelId);

        return requests.stream().map(this::mapToResponseDto).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public DtoLeaveRequestResponse updateLeaveStatus(Long leaveId, LeaveStatus newStatus, String managerNote) {
        Optional<LeaveRequest> optional = leaveRequestRepository.findById(leaveId);
        if (optional.isEmpty()) {
            throw new RuntimeException("İzin talebi bulunamadı! ID: " + leaveId);
        }
        LeaveRequest leaveRequest = optional.get();
        leaveRequest.setLeaveStatus(newStatus);

        if (managerNote != null && !managerNote.trim().isEmpty()) {
            leaveRequest.setManagerNote(managerNote);
        }

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return mapToResponseDto(updated);

    }

    @Override
    @Transactional
    public void deleteLeaveRequest(Long leaveId) {
        Optional<LeaveRequest> optional = leaveRequestRepository.findById(leaveId);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("İzin talebi bulunamadı! ID: " + leaveId);
        }
        LeaveRequest leaveRequest = optional.get();
        leaveRequestRepository.delete(leaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public DtoLeaveRequestResponse getLeaveRequestById(Long leaveId) {
        Optional<LeaveRequest> optional = leaveRequestRepository.findById(leaveId);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("İzin isteği bulunamadı! ID: " + leaveId);
        }
        LeaveRequest leaveRequest = optional.get();
        return mapToResponseDto(leaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoLeaveRequestResponse> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoLeaveRequestResponse> getLeaveRequestsByStatus(LeaveStatus status) {
        return leaveRequestRepository.findByLeaveStatus(status).stream().map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private int calculateDays(LocalDate startDate, LocalDate endDate) {
        int businessDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                businessDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return businessDays;
    }

    private DtoLeaveRequestResponse mapToResponseDto(LeaveRequest leaveRequest) {
        DtoLeaveRequestResponse dto = new DtoLeaveRequestResponse();
        dto.setId(leaveRequest.getId());
        dto.setPersonelId(leaveRequest.getPersonel().getId());
        dto.setFirstName(leaveRequest.getPersonel().getFirstName());
        dto.setLastName(leaveRequest.getPersonel().getLastName());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setRequestedDays(leaveRequest.getRequestDays());
        dto.setManagerNote(leaveRequest.getManagerNote());
        dto.setLeaveStatus(leaveRequest.getLeaveStatus());
        return dto;
    }
}
