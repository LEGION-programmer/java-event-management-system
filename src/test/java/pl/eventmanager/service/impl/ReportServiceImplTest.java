package pl.eventmanager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.eventmanager.dto.EventReportResponse;
import pl.eventmanager.dto.PopularityReportResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.exception.EventNotFoundException;
import pl.eventmanager.repository.EventRepository;
import pl.eventmanager.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Event buildEvent(Long id, String title, int capacity) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setLocation("Kraków");
        event.setEventDate(LocalDateTime.now().plusDays(30));
        event.setCapacity(capacity);
        event.setAvailableSeats(capacity);
        return event;
    }

    @Test
    void shouldReturnParticipationReport() {
        Event event = buildEvent(1L, "Big Conference", 100);
        event.setAvailableSeats(90);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(reservationRepository.countByEventIdAndStatus(1L, ReservationStatus.ACTIVE))
                .thenReturn(10L);

        EventReportResponse report = reportService.getEventParticipationReport(1L);

        assertEquals(1L, report.getEventId());
        assertEquals("Big Conference", report.getEventTitle());
        assertEquals(10L, report.getActiveReservations());
        assertEquals(90, report.getAvailableSeats());
        assertEquals(10.0, report.getOccupancyPercent());
    }

    @Test
    void shouldThrowWhenEventNotFoundInParticipationReport() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> reportService.getEventParticipationReport(999L));
    }

    @Test
    void shouldReturnPopularityReportSortedByActiveReservations() {
        Event e1 = buildEvent(1L, "Small Event", 50);
        Event e2 = buildEvent(2L, "Popular Event", 200);

        when(eventRepository.findAll()).thenReturn(List.of(e1, e2));
        when(reservationRepository.countByEventIdAndStatus(1L, ReservationStatus.ACTIVE))
                .thenReturn(5L);
        when(reservationRepository.countByEventIdAndStatus(2L, ReservationStatus.ACTIVE))
                .thenReturn(40L);

        List<PopularityReportResponse> report = reportService.getEventsPopularityReport();

        assertEquals(2, report.size());
        assertEquals(2L, report.get(0).getEventId());
        assertEquals(1L, report.get(1).getEventId());
    }
}