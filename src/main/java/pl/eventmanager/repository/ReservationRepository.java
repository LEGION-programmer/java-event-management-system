package pl.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eventmanager.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}