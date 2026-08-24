package com.aslan.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DtoPersonelResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthOfDate;
    private String email;
    private Long registrationNumber;

    private String departmentName;
    private String roleName;
    private List<String> skills;

    private String profilePhoto;
    private boolean isActive;

}
