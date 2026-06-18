package com.volunteer.service;

import com.volunteer.dto.VolunteerDto;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.Volunteer.VolunteerStatus;
import com.volunteer.exception.DuplicateResourceException;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Transactional
    public VolunteerDto register(VolunteerDto dto) {
        if (volunteerRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already registered as a volunteer: " + dto.getEmail());
        }
        Volunteer saved = volunteerRepository.save(toEntity(dto));
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<VolunteerDto> getAll() {
        return volunteerRepository.findAll()
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VolunteerDto getById(Long id) {
        Volunteer volunteer = findOrThrow(id);
        return toDto(volunteer);
    }

    @Transactional(readOnly = true)
    public VolunteerDto trackStatus(String email) {
        Volunteer volunteer = volunteerRepository.findByEmail(email.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No application found matching that email and phone number."));
        return toDto(volunteer);
    }

    private boolean phoneMatches(String stored, String supplied) {
        if (stored == null || supplied == null) return false;
        return digitsOnly(stored).equals(digitsOnly(supplied));
    }

    private String digitsOnly(String value) {
        return value.replaceAll("\\D", "");
    }

    @Transactional(readOnly = true)
    public List<VolunteerDto> getByStatus(VolunteerStatus status) {
        return volunteerRepository.findByStatus(status)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public VolunteerDto updateStatus(Long id, String status) {
        Volunteer volunteer = findOrThrow(id);
        try {
            volunteer.setStatus(VolunteerStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
        return toDto(volunteerRepository.save(volunteer));
    }

    @Transactional
    public VolunteerDto update(Long id, VolunteerDto dto) {
        Volunteer volunteer = findOrThrow(id);

        volunteer.setFirstName(dto.getFirstName());
        volunteer.setLastName(dto.getLastName());
        volunteer.setPhone(dto.getPhone());
        volunteer.setAddress(dto.getAddress());
        volunteer.setCity(dto.getCity());
        volunteer.setState(dto.getState());
        volunteer.setAvailability(dto.getAvailability());
        volunteer.setSkills(dto.getSkills());
        volunteer.setHoursPerWeek(dto.getHoursPerWeek());
        volunteer.setMotivation(dto.getMotivation());
        volunteer.setEmergencyContactName(dto.getEmergencyContactName());
        volunteer.setEmergencyContactPhone(dto.getEmergencyContactPhone());

        if (dto.getTotalHoursLogged() != null) {
            volunteer.setTotalHoursLogged(dto.getTotalHoursLogged());
        }

        return toDto(volunteerRepository.save(volunteer));
    }

    @Transactional
    public void delete(Long id) {
        if (!volunteerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Volunteer not found with id: " + id);
        }
        volunteerRepository.deleteById(id);
    }

    // ── Mapping ──────────────────────────────────────────────────────────────

    private Volunteer findOrThrow(Long id) {
        return volunteerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
    }

    public Volunteer toEntity(VolunteerDto dto) {
        Volunteer v = new Volunteer();
        v.setFirstName(dto.getFirstName());
        v.setLastName(dto.getLastName());
        v.setEmail(dto.getEmail());
        v.setPhone(dto.getPhone());
        v.setAddress(dto.getAddress());
        v.setCity(dto.getCity());
        v.setState(dto.getState());
        v.setDateOfBirth(dto.getDateOfBirth());
        v.setAvailability(dto.getAvailability());
        v.setSkills(dto.getSkills());
        v.setMotivation(dto.getMotivation());
        v.setHoursPerWeek(dto.getHoursPerWeek());
        v.setEmergencyContactName(dto.getEmergencyContactName());
        v.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        return v;
    }

    public VolunteerDto toDto(Volunteer v) {
        return VolunteerDto.builder()
            .id(v.getId())
            .firstName(v.getFirstName())
            .lastName(v.getLastName())
            .email(v.getEmail())
            .phone(v.getPhone())
            .address(v.getAddress())
            .city(v.getCity())
            .state(v.getState())
            .dateOfBirth(v.getDateOfBirth())
            .availability(v.getAvailability())
            .skills(v.getSkills())
            .motivation(v.getMotivation())
            .hoursPerWeek(v.getHoursPerWeek())
            .status(v.getStatus() != null ? v.getStatus().name() : null)
            .emergencyContactName(v.getEmergencyContactName())
            .emergencyContactPhone(v.getEmergencyContactPhone())
            .registrationDate(v.getRegistrationDate())
            .totalHoursLogged(v.getTotalHoursLogged())
            .build();
    }
}
