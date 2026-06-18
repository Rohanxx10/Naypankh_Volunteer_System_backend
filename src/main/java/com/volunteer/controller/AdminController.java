package com.volunteer.controller;

import com.volunteer.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/reports/volunteers-by-city")
    public ResponseEntity<List<Map<String, Object>>> getVolunteersByCity() {
        return ResponseEntity.ok(adminService.getVolunteersByCity());
    }

    @GetMapping("/reports/volunteers-by-availability")
    public ResponseEntity<List<Map<String, Object>>> getVolunteersByAvailability() {
        return ResponseEntity.ok(adminService.getVolunteersByAvailability());
    }

    @GetMapping("/reports/monthly-registrations")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRegistrations() {
        return ResponseEntity.ok(adminService.getMonthlyRegistrations());
    }

    @GetMapping("/reports/skills-summary")
    public ResponseEntity<List<Map<String, Object>>> getSkillsSummary() {
        return ResponseEntity.ok(adminService.getSkillsSummary());
    }

    @GetMapping("/reports/status-breakdown")
    public ResponseEntity<List<Map<String, Object>>> getStatusBreakdown() {
        return ResponseEntity.ok(adminService.getStatusBreakdown());
    }
}
