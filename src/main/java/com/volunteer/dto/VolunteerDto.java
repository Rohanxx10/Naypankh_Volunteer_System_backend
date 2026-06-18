package com.volunteer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private LocalDate dateOfBirth;
    private String availability;
    private List<String> skills;
    private String motivation;
    private Integer hoursPerWeek;
    private String status;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private LocalDate registrationDate;
    private Integer totalHoursLogged;
}
