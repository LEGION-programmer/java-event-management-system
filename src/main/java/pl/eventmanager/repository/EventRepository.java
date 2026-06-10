package pl.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eventmanager.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}