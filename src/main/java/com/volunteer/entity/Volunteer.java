package com.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "volunteers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String address;
    private String city;
    private String state;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String availability;  // WEEKDAYS, WEEKENDS, BOTH, FLEXIBLE

    @ElementCollection
    @CollectionTable(name = "volunteer_skills", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Column(name = "skill")
    private List<String> skills;

    @Column(length = 1000)
    private String motivation;

    private Integer hoursPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VolunteerStatus status;

    private String emergencyContactName;
    private String emergencyContactPhone;

    private LocalDate registrationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer totalHoursLogged;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Only set defaults when the field hasn't been explicitly provided
        // (allows DataSeeder to supply its own values without being overwritten)
        if (registrationDate == null)  registrationDate  = LocalDate.now();
        if (status == null)            status            = VolunteerStatus.PENDING;
        if (totalHoursLogged == null)  totalHoursLogged  = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum VolunteerStatus {
        PENDING, APPROVED, REJECTED, INACTIVE
    }
}