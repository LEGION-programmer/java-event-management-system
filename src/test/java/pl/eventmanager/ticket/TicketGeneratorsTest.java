package pl.eventmanager.ticket;

import org.junit.jupiter.api.Test;
import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.ReservationStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketGeneratorsTest {

    private Reservation buildReservation(Long reservationId, Long eventId, int capacity, String username) {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn(username);

        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Test Event");
        event.setLocation("Kraków");
        event.setEventDate(LocalDateTime.of(2026, 9, 1, 10, 0));
        event.setCapacity(capacity);
        event.setAvailableSeats(capacity);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getId()).thenReturn(reservationId);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getEvent()).thenReturn(event);
        when(reservation.getStatus()).thenReturn(ReservationStatus.ACTIVE);

        return reservation;
    }

    @Test
    void standardGeneratorShouldProduceTicketWithoutMessage() {
        Reservation reservation = buildReservation(1L, 10L, 100, "jan");
        TicketGenerator generator = new StandardTicketGenerator();

        TicketResponse ticket = generator.generate(reservation);

        assertNotNull(ticket);
        assertEquals(1L, ticket.getReservationId());
        assertEquals("EVT-00010-RES-00001", ticket.getTicketNumber());
        assertEquals("Test Event", ticket.getEventTitle());
        assertEquals("jan", ticket.getHolderName());
        assertNull(ticket.getMessage());
        assertNotNull(ticket.getIssuedAt());
    }

    @Test
    void personalizedGeneratorShouldProduceTicketWithMessage() {
        Reservation reservation = buildReservation(2L, 5L, 20, "anna");
        TicketGenerator generator = new PersonalizedTicketGenerator();

        TicketResponse ticket = generator.generate(reservation);

        assertNotNull(ticket);
        assertEquals(2L, ticket.getReservationId());
        assertEquals("EVT-00005-RES-00002", ticket.getTicketNumber());
        assertEquals("anna", ticket.getHolderName());
        assertNotNull(ticket.getMessage());
        assertTrue(ticket.getMessage().contains("anna"));
        assertTrue(ticket.getMessage().contains("Test Event"));
    }
}