package com.aslan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personel")
public class Personel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length =50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_of_date", nullable = true)
    private LocalDate birthOfDate;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "registration_number", nullable = false)
    private Long registrationNumber;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Role role;

    @ManyToMany
    private List<Skill> skills;

    @Column(name = "profile_photo", nullable = true, columnDefinition = "TEXT")
    private String profilePhoto;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "personel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveRequest> leaveRequests;

    @OneToMany(mappedBy = "personel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomReservation> roomReservations;
}
