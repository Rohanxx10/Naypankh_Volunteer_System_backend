package com.volunteer.controller;

import com.volunteer.dto.VolunteerDto;
import com.volunteer.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;

    @PostMapping("/register")
    public ResponseEntity<VolunteerDto> register(@RequestBody VolunteerDto dto) {
        return ResponseEntity.status(201).body(volunteerService.register(dto));
    }

    @GetMapping("/track")
    public ResponseEntity<VolunteerDto> track(@RequestParam String email) {
        return ResponseEntity.ok(volunteerService.trackStatus(email));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VolunteerDto>> getAll() {
        return ResponseEntity.ok(volunteerService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VolunteerDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VolunteerDto> update(@PathVariable Long id,
                                               @RequestBody VolunteerDto dto) {
        return ResponseEntity.ok(volunteerService.update(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VolunteerDto> updateStatus(@PathVariable Long id,
                                                     @RequestParam String status) {
        return ResponseEntity.ok(volunteerService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        volunteerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
