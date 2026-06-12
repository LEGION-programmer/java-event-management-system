package pl.eventmanager.service;

import pl.eventmanager.dto.EventReportResponse;
import pl.eventmanager.dto.PopularityReportResponse;

import java.util.List;

public interface ReportService {

    EventReportResponse getEventParticipationReport(Long eventId);

    List<PopularityReportResponse> getEventsPopularityReport();
}