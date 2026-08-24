package com.aslan.controller;

import com.aslan.dto.DtoLeaveRequestCreate;
import com.aslan.dto.DtoLeaveRequestResponse;
import com.aslan.enums.LeaveStatus;
import com.aslan.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-request")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<DtoLeaveRequestResponse> createLeaveRequest(@Valid @RequestBody DtoLeaveRequestCreate dto){
        DtoLeaveRequestResponse response = leaveRequestService.createLeaveRequest(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/personel/{personelId}")
    public ResponseEntity<List<DtoLeaveRequestResponse>> getLeaveRequestsByPersonelId(@PathVariable Long personelId){
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByPersonelId(personelId));
    }

    @PutMapping("/update/status/{leaveId}")
    public ResponseEntity<DtoLeaveRequestResponse> updateLeaveStatus(@PathVariable Long leaveId, @RequestParam LeaveStatus newStatus,@RequestParam(required = false) String managerNote){
        return ResponseEntity.ok(leaveRequestService.updateLeaveStatus(leaveId, newStatus, managerNote));
    }

    @DeleteMapping("/delete/{leaveId}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long leaveId){
        leaveRequestService.deleteLeaveRequest(leaveId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{leaveId}")
    public ResponseEntity<DtoLeaveRequestResponse> getLeaveRequestById(@PathVariable Long leaveId){
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestById(leaveId));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DtoLeaveRequestResponse>> getAllLeaveRequests(){
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }


    @GetMapping("/get/status/{status}")
    public ResponseEntity<List<DtoLeaveRequestResponse>> getLeaveRequestsByStatus(@PathVariable LeaveStatus status){
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByStatus(status));
    }

}
