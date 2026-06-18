package com.volunteer.service;

import com.volunteer.entity.Event;
import com.volunteer.entity.Event.EventStatus;
import com.volunteer.entity.Volunteer;
import com.volunteer.exception.DuplicateResourceException;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.EventRepository;
import com.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final VolunteerRepository volunteerRepository;

    @Transactional(readOnly = true)
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Event> getByStatus(EventStatus status) {
        return eventRepository.findByStatus(status);
    }

    @Transactional
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public Event update(Long id, Event updated) {
        Event existing = getById(id);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setLocation(updated.getLocation());
        existing.setStartDateTime(updated.getStartDateTime());
        existing.setEndDateTime(updated.getEndDateTime());
        existing.setMaxVolunteers(updated.getMaxVolunteers());
        existing.setStatus(updated.getStatus());
        existing.setCategory(updated.getCategory());

        return eventRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    @Transactional
    public Event registerVolunteer(Long eventId, Long volunteerId) {
        Event event = getById(eventId);

        // ── Duplicate-assignment guard ────────────────────────────────────────
        boolean alreadyAssigned = event.getVolunteers().stream()
                .anyMatch(v -> v.getId().equals(volunteerId));
        if (alreadyAssigned) {
            throw new DuplicateResourceException(
                    "Volunteer #" + volunteerId + " is already assigned to this event.");
        }

        // ── Capacity guard ────────────────────────────────────────────────────
        if (event.getMaxVolunteers() != null
                && event.getRegisteredVolunteers() >= event.getMaxVolunteers()) {
            throw new IllegalStateException("Event is already at full capacity.");
        }

        // ── Look up the volunteer ─────────────────────────────────────────────
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Volunteer not found with id: " + volunteerId));

        // ── Assign and persist ────────────────────────────────────────────────
        event.getVolunteers().add(volunteer);
        event.setRegisteredVolunteers(event.getVolunteers().size()); // keep counter in sync
        return eventRepository.save(event);
    }


    @Transactional(readOnly = true)
    public List<Long> getAssignedVolunteerIds(Long eventId) {
        Event event = getById(eventId);
        return event.getVolunteers().stream()
                .map(Volunteer::getId)
                .toList();
    }
}