package pl.eventmanager.ticket;

import org.springframework.stereotype.Component;
import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;

import java.time.LocalDateTime;

@Component("personalizedTicketGenerator")
public class PersonalizedTicketGenerator implements TicketGenerator {

    @Override
    public TicketResponse generate(Reservation reservation) {
        Event event = reservation.getEvent();
        String ticketNumber = "EVT-%05d-RES-%05d".formatted(
                event.getId(), reservation.getId()
        );
        String message = "Dear %s, we look forward to seeing you at %s!"
                .formatted(reservation.getUser().getUsername(), event.getTitle());
        return new TicketResponse(
                reservation.getId(),
                ticketNumber,
                event.getTitle(),
                event.getLocation(),
                event.getEventDate(),
                reservation.getUser().getUsername(),
                message,
                LocalDateTime.now()
        );
    }
}