package pl.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.eventmanager.dto.EventReportResponse;
import pl.eventmanager.dto.PopularityReportResponse;
import pl.eventmanager.service.ReportService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock private ReportService reportService;
    @InjectMocks private ReportController reportController;

    @Test
    void shouldGetParticipationReport() {
        EventReportResponse report = new EventReportResponse(
                1L, "Test Event", LocalDateTime.now(),
                "Warsaw", 100, 10, 90, 10.0
        );
        when(reportService.getEventParticipationReport(1L)).thenReturn(report);

        EventReportResponse result = reportController.getEventParticipation(1L);

        assertEquals(1L, result.getEventId());
        assertEquals(10L, result.getActiveReservations());
    }

    @Test
    void shouldGetPopularityReport() {
        when(reportService.getEventsPopularityReport()).thenReturn(List.of());

        List<PopularityReportResponse> result = reportController.getEventsPopularity();

        assertNotNull(result);
    }
}