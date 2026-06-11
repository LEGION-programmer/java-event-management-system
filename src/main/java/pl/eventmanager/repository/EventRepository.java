package pl.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eventmanager.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime dateTime);
}