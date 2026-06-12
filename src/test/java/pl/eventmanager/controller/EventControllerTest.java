package pl.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.eventmanager.dto.CreateEventRequest;
import pl.eventmanager.dto.EventResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.User;
import pl.eventmanager.service.EventService;
import pl.eventmanager.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock private EventService eventService;
    @Mock private UserService userService;
    @InjectMocks private EventController eventController;

    @Test
    void shouldReturnAllEvents() {
        when(eventService.findAll()).thenReturn(List.of());

        List<EventResponse> result = eventController.getAllEvents();

        assertNotNull(result);
    }

    @Test
    void shouldCreateEventAsAdmin() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");

        User creator = mock(User.class);
        when(userService.findById(1L)).thenReturn(Optional.of(creator));

        Event saved = new Event();
        saved.setId(1L);
        saved.setTitle("Test Event");
        saved.setLocation("Warsaw");
        saved.setEventDate(LocalDateTime.of(2026, 9, 1, 10, 0));
        saved.setCapacity(100);
        saved.setAvailableSeats(100);
        when(eventService.save(any(Event.class))).thenReturn(saved);

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test Event");
        request.setLocation("Warsaw");
        request.setEventDate(LocalDateTime.of(2026, 9, 1, 10, 0));
        request.setCapacity(100);

        ResponseEntity<EventResponse> result = eventController.createEvent(request, jwt);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Test Event", result.getBody().getTitle());
    }
}