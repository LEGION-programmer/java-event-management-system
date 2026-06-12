package pl.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.service.TicketService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock private TicketService ticketService;
    @InjectMocks private TicketController ticketController;

    @Test
    void shouldGetTicket() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");

        TicketResponse ticket = new TicketResponse(
                1L, "EVT-00001-RES-00001", "Test Event",
                "Warsaw", LocalDateTime.now(), "jan",
                null, LocalDateTime.now()
        );
        when(ticketService.getTicket(1L, 1L)).thenReturn(ticket);

        TicketResponse result = ticketController.getTicket(1L, jwt);

        assertNotNull(result);
        assertEquals("EVT-00001-RES-00001", result.getTicketNumber());
    }
}