package pl.eventmanager.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.eventmanager.dto.CreateEventRequest;
import pl.eventmanager.entity.Event;
import pl.eventmanager.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Event createEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {

        Event event = new Event();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setEventDate(request.getEventDate());

        event.setCapacity(request.getCapacity());
        event.setAvailableSeats(request.getCapacity());

        event.setCreatedAt(LocalDateTime.now());

        return eventService.save(event);
    }
}