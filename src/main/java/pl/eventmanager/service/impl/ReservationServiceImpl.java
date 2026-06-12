package pl.eventmanager.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eventmanager.dto.ReservationResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.exception.EventNotFoundException;
import pl.eventmanager.exception.ReservationAlreadyActiveException;
import pl.eventmanager.exception.ReservationNotFoundException;
import pl.eventmanager.exception.SeatUnavailableException;
import pl.eventmanager.repository.EventRepository;
import pl.eventmanager.repository.ReservationRepository;
import pl.eventmanager.repository.UserRepository;
import pl.eventmanager.service.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            EventRepository eventRepository,
            UserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ReservationResponse book(Long userId, Long eventId) {
        if (reservationRepository.existsByUserIdAndEventIdAndStatus(
                userId, eventId, ReservationStatus.ACTIVE)) {
            throw new ReservationAlreadyActiveException(eventId);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getAvailableSeats() <= 0) {
            throw new SeatUnavailableException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found: " + userId
                ));

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEvent(event);

        Reservation saved = reservationRepository.save(reservation);

        return mapToResponse(saved, event);
    }

    @Override
    @Transactional
    public void cancel(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(
                    "Cannot cancel another user's reservation"
            );
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        Event event = reservation.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllByUserId(Long userId) {
        return reservationRepository
                .findAllByUserIdOrderByReservationDateDesc(userId)
                .stream()
                .map(r -> mapToResponse(r, r.getEvent()))
                .collect(Collectors.toList());
    }

    private ReservationResponse mapToResponse(Reservation reservation, Event event) {
        return new ReservationResponse(
                reservation.getId(),
                event.getId(),
                event.getTitle(),
                event.getEventDate(),
                event.getLocation(),
                reservation.getStatus(),
                reservation.getReservationDate()
        );
    }
}