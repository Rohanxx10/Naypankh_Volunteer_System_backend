package com.volunteer.controller;

import com.volunteer.entity.Event;
import com.volunteer.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

     @GetMapping
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> create(@RequestBody Event event) {
        return ResponseEntity.status(201).body(eventService.create(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> update(@PathVariable Long id,
                                        @RequestBody Event updated) {
        return ResponseEntity.ok(eventService.update(id, updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

     @PostMapping("/{eventId}/volunteers/{volunteerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> registerVolunteer(@PathVariable Long eventId,
                                                   @PathVariable Long volunteerId) {
        return ResponseEntity.ok(eventService.registerVolunteer(eventId, volunteerId));
    }

     @GetMapping("/{eventId}/volunteers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Long>> getAssignedVolunteerIds(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAssignedVolunteerIds(eventId));
    }
}