package pl.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.enums.ReservationStatus;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByUserIdAndEventIdAndStatus(
            Long userId,
            Long eventId,
            ReservationStatus status
    );

    boolean existsByUserIdAndEventIdAndStatus(
            Long userId,
            Long eventId,
            ReservationStatus status
    );

    List<Reservation> findAllByUserIdOrderByReservationDateDesc(Long userId);

    List<Reservation> findAllByEventIdOrderByReservationDateAsc(Long eventId);

    long countByEventIdAndStatus(Long eventId, ReservationStatus status);
}