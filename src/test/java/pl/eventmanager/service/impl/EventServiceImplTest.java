package pl.eventmanager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.eventmanager.entity.Event;
import pl.eventmanager.repository.EventRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void shouldSaveEvent() {
        Event event = new Event();
        when(repository.save(event)).thenReturn(event);

        Event result = eventService.save(event);

        assertSame(event, result);
        verify(repository).save(event);
    }

    @Test
    void shouldReturnAllEvents() {
        Event e1 = new Event();
        Event e2 = new Event();
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<Event> result = eventService.findAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldFindEventById() {
        Long id = 1L;
        Event event = new Event();

        when(repository.findById(id)).thenReturn(Optional.of(event));

        Optional<Event> result = eventService.findById(id);

        assertTrue(result.isPresent());
        assertSame(event, result.get());
        verify(repository).findById(id);
    }

    @Test
    void shouldDeleteEvent() {
        Long id = 1L;

        eventService.delete(id);

        verify(repository).deleteById(id);
    }
}