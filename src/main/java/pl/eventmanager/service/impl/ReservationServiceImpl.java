package pl.eventmanager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eventmanager.dto.ReservationResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.repository.EventRepository;
import pl.eventmanager.repository.ReservationRepository;
import pl.eventmanager.repository.UserRepository;
import pl.eventmanager.service.ReservationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationResponse reserveEvent(Long eventId, String username) {

        User user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        boolean alreadyReserved =
                reservationRepository.existsByUserIdAndEventIdAndStatus(
                        user.getId(),
                        eventId,
                        ReservationStatus.ACTIVE
                );

        if (alreadyReserved) {
            throw new IllegalStateException("User already reserved this event");
        }

        if (event.getAvailableSeats() <= 0) {
            throw new IllegalStateException("No available seats");
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEvent(event);
        reservation.setStatus(ReservationStatus.ACTIVE);

        reservation = reservationRepository.save(reservation);

        return map(reservation);
    }

    @Override
    public List<ReservationResponse> getUserReservations(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        return reservationRepository
                .findAllByUserIdOrderByReservationDateDesc(user.getId())
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not your reservation");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        Event event = reservation.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
    }

    private ReservationResponse map(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getEvent().getId(),
                r.getEvent().getTitle(),
                r.getReservationDate(),
                r.getStatus().name()
        );
    }
}