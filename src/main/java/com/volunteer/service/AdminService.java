package com.volunteer.service;

import com.volunteer.entity.Event;
import com.volunteer.entity.Volunteer;
import com.volunteer.entity.Volunteer.VolunteerStatus;
import com.volunteer.repository.EventRepository;
import com.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final VolunteerRepository volunteerRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Volunteer counts
        stats.put("totalVolunteers",    volunteerRepository.count());
        stats.put("approvedVolunteers", volunteerRepository.countByStatus(VolunteerStatus.APPROVED));
        stats.put("pendingVolunteers",  volunteerRepository.countByStatus(VolunteerStatus.PENDING));
        stats.put("rejectedVolunteers", volunteerRepository.countByStatus(VolunteerStatus.REJECTED));
        stats.put("inactiveVolunteers", volunteerRepository.countByStatus(VolunteerStatus.INACTIVE));

        // New volunteers this month
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        stats.put("newVolunteersThisMonth",
                volunteerRepository.countByRegistrationDateBetween(monthStart, LocalDate.now()));

        // Total hours contributed
        int totalHours = volunteerRepository.findAll().stream()
                .mapToInt(v -> v.getTotalHoursLogged() != null ? v.getTotalHoursLogged() : 0)
                .sum();
        stats.put("totalHoursLogged", totalHours);

        // Event counts
        stats.put("totalEvents",     eventRepository.count());
        stats.put("upcomingEvents",  eventRepository.countByStatus(Event.EventStatus.UPCOMING));
        stats.put("ongoingEvents",   eventRepository.countByStatus(Event.EventStatus.ONGOING));
        stats.put("completedEvents", eventRepository.countByStatus(Event.EventStatus.COMPLETED));
        stats.put("cancelledEvents", eventRepository.countByStatus(Event.EventStatus.CANCELLED));

        return stats;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getVolunteersByCity() {
        List<Object[]> rows = volunteerRepository.countByCity();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("city",  row[0]);
            item.put("count", row[1]);
            result.add(item);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getVolunteersByAvailability() {
        List<Object[]> rows = volunteerRepository.countByAvailability();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("availability", row[0]);
            item.put("count",        row[1]);
            result.add(item);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyRegistrations() {
        int year = LocalDate.now().getYear();
        List<Object[]> rows = volunteerRepository.monthlyRegistrations(year);

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        // Pre-fill all 12 months with 0
        Map<Integer, Long> byMonth = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) byMonth.put(i, 0L);

        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            byMonth.put(month, count);
        }

        // If every month is zero there is genuinely no data yet — return []
        // so the frontend EmptyState triggers instead of a flat zero line.
        boolean hasData = byMonth.values().stream().anyMatch(c -> c > 0);
        if (!hasData) return new ArrayList<>();

        List<Map<String, Object>> result = new ArrayList<>();
        byMonth.forEach((m, count) -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("month", months[m - 1]);
            item.put("count", count);
            result.add(item);
        });
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSkillsSummary() {
        List<Volunteer> approved = volunteerRepository.findByStatus(VolunteerStatus.APPROVED);

        Map<String, Long> skillCount = new TreeMap<>();
        for (Volunteer v : approved) {
            if (v.getSkills() != null) {
                for (String skill : v.getSkills()) {
                    skillCount.merge(skill.trim(), 1L, Long::sum);
                }
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        skillCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(e -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("skill", e.getKey());
                    item.put("count", e.getValue());
                    result.add(item);
                });
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusBreakdown() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (VolunteerStatus status : VolunteerStatus.values()) {
            long count = volunteerRepository.countByStatus(status);
            if (count == 0) continue; // skip zero slices — Recharts renders them invisible
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("status", status.name());
            item.put("count",  count);
            result.add(item);
        }
        return result;
    }
}