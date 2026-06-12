package pl.eventmanager.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.exception.ReservationNotFoundException;
import pl.eventmanager.repository.ReservationRepository;
import pl.eventmanager.service.TicketService;
import pl.eventmanager.ticket.TicketGenerator;

@Service
public class TicketServiceImpl implements TicketService {

    private final ReservationRepository reservationRepository;
    private final TicketGenerator standardTicketGenerator;
    private final TicketGenerator personalizedTicketGenerator;

    public TicketServiceImpl(
            ReservationRepository reservationRepository,
            @Qualifier("standardTicketGenerator") TicketGenerator standardTicketGenerator,
            @Qualifier("personalizedTicketGenerator") TicketGenerator personalizedTicketGenerator
    ) {
        this.reservationRepository = reservationRepository;
        this.standardTicketGenerator = standardTicketGenerator;
        this.personalizedTicketGenerator = personalizedTicketGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicket(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(
                    "Cannot access another user's ticket"
            );
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot generate ticket for cancelled reservation"
            );
        }

        return selectGenerator(reservation.getEvent()).generate(reservation);
    }

    private TicketGenerator selectGenerator(Event event) {
        return event.getCapacity() <= 50
                ? personalizedTicketGenerator
                : standardTicketGenerator;
    }
}