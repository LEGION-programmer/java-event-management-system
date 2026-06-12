package pl.eventmanager.service;

import pl.eventmanager.dto.TicketResponse;

public interface TicketService {

    TicketResponse getTicket(Long reservationId, Long userId);
}