package pl.eventmanager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import pl.eventmanager.dto.TicketResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.exception.ReservationNotFoundException;
import pl.eventmanager.repository.ReservationRepository;
import pl.eventmanager.ticket.PersonalizedTicketGenerator;
import pl.eventmanager.ticket.StandardTicketGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TicketServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(
                reservationRepository,
                new StandardTicketGenerator(),
                new PersonalizedTicketGenerator()
        );
    }

    private Reservation buildReservation(Long reservationId, Long userId, int capacity, ReservationStatus status) {
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        when(user.getUsername()).thenReturn("jan");

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setLocation("Kraków");
        event.setEventDate(LocalDateTime.now().plusDays(7));
        event.setCapacity(capacity);
        event.setAvailableSeats(capacity);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getId()).thenReturn(reservationId);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getEvent()).thenReturn(event);
        when(reservation.getStatus()).thenReturn(status);

        return reservation;
    }

    @Test
    void shouldReturnStandardTicketForLargeEvent() {
        Reservation reservation = buildReservation(1L, 1L, 100, ReservationStatus.ACTIVE);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        TicketResponse ticket = ticketService.getTicket(1L, 1L);

        assertNotNull(ticket);
        assertNull(ticket.getMessage());
    }

    @Test
    void shouldReturnPersonalizedTicketForSmallEvent() {
        Reservation reservation = buildReservation(2L, 1L, 20, ReservationStatus.ACTIVE);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));

        TicketResponse ticket = ticketService.getTicket(2L, 1L);

        assertNotNull(ticket.getMessage());
        assertTrue(ticket.getMessage().contains("jan"));
    }

    @Test
    void shouldThrowWhenReservationNotFoundForTicket() {
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> ticketService.getTicket(999L, 1L));
    }

    @Test
    void shouldThrowWhenAccessingAnotherUsersTicket() {
        Reservation reservation = buildReservation(1L, 1L, 100, ReservationStatus.ACTIVE);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(AccessDeniedException.class,
                () -> ticketService.getTicket(1L, 2L));
    }

    @Test
    void shouldThrowWhenReservationIsCancelled() {
        Reservation reservation = buildReservation(1L, 1L, 100, ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class,
                () -> ticketService.getTicket(1L, 1L));
    }
}