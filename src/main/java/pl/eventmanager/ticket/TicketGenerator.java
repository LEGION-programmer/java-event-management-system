package pl.eventmanager.ticket;

import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.entity.Reservation;

public interface TicketGenerator {

    TicketResponse generate(Reservation reservation);
}