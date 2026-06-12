package pl.eventmanager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.service.TicketService;

@RestController
@RequestMapping("/api/reservations")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{reservationId}/ticket")
    @PreAuthorize("isAuthenticated()")
    public TicketResponse getTicket(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = Long.parseLong(jwt.getSubject());
        return ticketService.getTicket(reservationId, userId);
    }
}