package pl.eventmanager.service;

import pl.eventmanager.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Event save(Event event);

    List<Event> findAll();

    Optional<Event> findById(Long id);

    void delete(Long id);
}