package pl.eventmanager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.eventmanager.dto.EventReportResponse;
import pl.eventmanager.dto.PopularityReportResponse;
import pl.eventmanager.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/events/{eventId}/participation")
    @PreAuthorize("hasRole('ADMIN')")
    public EventReportResponse getEventParticipation(@PathVariable Long eventId) {
        return reportService.getEventParticipationReport(eventId);
    }

    @GetMapping("/events/popularity")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PopularityReportResponse> getEventsPopularity() {
        return reportService.getEventsPopularityReport();
    }
}