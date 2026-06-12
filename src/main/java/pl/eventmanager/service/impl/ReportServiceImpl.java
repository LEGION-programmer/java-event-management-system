package pl.eventmanager.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eventmanager.dto.EventReportResponse;
import pl.eventmanager.dto.PopularityReportResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.exception.EventNotFoundException;
import pl.eventmanager.repository.EventRepository;
import pl.eventmanager.repository.ReservationRepository;
import pl.eventmanager.service.ReportService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    public ReportServiceImpl(
            EventRepository eventRepository,
            ReservationRepository reservationRepository
    ) {
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public EventReportResponse getEventParticipationReport(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        long activeReservations = reservationRepository.countByEventIdAndStatus(
                eventId, ReservationStatus.ACTIVE
        );

        double occupancy = event.getCapacity() > 0
                ? Math.round((activeReservations * 100.0 / event.getCapacity()) * 10.0) / 10.0
                : 0.0;

        return new EventReportResponse(
                event.getId(),
                event.getTitle(),
                event.getEventDate(),
                event.getLocation(),
                event.getCapacity(),
                activeReservations,
                event.getAvailableSeats(),
                occupancy
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PopularityReportResponse> getEventsPopularityReport() {
        return eventRepository.findAll().stream()
                .map(event -> {
                    long count = reservationRepository.countByEventIdAndStatus(
                            event.getId(), ReservationStatus.ACTIVE
                    );
                    double occupancy = event.getCapacity() > 0
                            ? Math.round((count * 100.0 / event.getCapacity()) * 10.0) / 10.0
                            : 0.0;
                    return new PopularityReportResponse(
                            event.getId(),
                            event.getTitle(),
                            event.getEventDate(),
                            event.getLocation(),
                            event.getCapacity(),
                            count,
                            occupancy
                    );
                })
                .sorted(Comparator.comparingLong(PopularityReportResponse::getActiveReservations).reversed())
                .collect(Collectors.toList());
    }
}