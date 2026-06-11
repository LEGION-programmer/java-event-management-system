package pl.eventmanager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import pl.eventmanager.dto.ReservationResponse;
import pl.eventmanager.entity.Event;
import pl.eventmanager.entity.Reservation;
import pl.eventmanager.entity.User;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.exception.EventNotFoundException;
import pl.eventmanager.exception.ReservationAlreadyActiveException;
import pl.eventmanager.exception.ReservationNotFoundException;
import pl.eventmanager.exception.SeatUnavailableException;
import pl.eventmanager.repository.EventRepository;
import pl.eventmanager.repository.ReservationRepository;
import pl.eventmanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Event buildEvent(Long id, int availableSeats, int capacity) {
        Event event = new Event();
        event.setId(id);
        event.setTitle("Sample Event");
        event.setLocation("Warsaw");
        event.setEventDate(LocalDateTime.now().plusDays(14));
        event.setCapacity(capacity);
        event.setAvailableSeats(availableSeats);
        return event;
    }

    @Test
    void shouldBookReservationSuccessfully() {
        Long userId = 1L;
        Long eventId = 10L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Event event = buildEvent(eventId, 5, 10);

        Reservation savedReservation = mock(Reservation.class);
        when(savedReservation.getId()).thenReturn(100L);
        when(savedReservation.getEvent()).thenReturn(event);
        when(savedReservation.getStatus()).thenReturn(ReservationStatus.ACTIVE);
        when(savedReservation.getReservationDate()).thenReturn(LocalDateTime.now());

        when(reservationRepository.existsByUserIdAndEventIdAndStatus(
                userId, eventId, ReservationStatus.ACTIVE)).thenReturn(false);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.save(event)).thenReturn(event);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        ReservationResponse response = reservationService.book(userId, eventId);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(eventId, response.getEventId());
        assertEquals(ReservationStatus.ACTIVE, response.getStatus());
        assertEquals(4, event.getAvailableSeats());
        verify(eventRepository).save(event);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldThrowWhenActiveReservationAlreadyExists() {
        Long userId = 1L;
        Long eventId = 10L;

        when(reservationRepository.existsByUserIdAndEventIdAndStatus(
                userId, eventId, ReservationStatus.ACTIVE)).thenReturn(true);

        assertThrows(ReservationAlreadyActiveException.class,
                () -> reservationService.book(userId, eventId));

        verifyNoInteractions(eventRepository);
    }

    @Test
    void shouldThrowWhenEventNotFound() {
        Long userId = 1L;
        Long eventId = 999L;

        when(reservationRepository.existsByUserIdAndEventIdAndStatus(
                userId, eventId, ReservationStatus.ACTIVE)).thenReturn(false);
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> reservationService.book(userId, eventId));

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowWhenNoSeatsAvailable() {
        Long userId = 1L;
        Long eventId = 10L;

        Event event = buildEvent(eventId, 0, 10);

        when(reservationRepository.existsByUserIdAndEventIdAndStatus(
                userId, eventId, ReservationStatus.ACTIVE)).thenReturn(false);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(SeatUnavailableException.class,
                () -> reservationService.book(userId, eventId));

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldCancelOwnReservationSuccessfully() {
        Long userId = 1L;
        Long reservationId = 100L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Event event = buildEvent(10L, 5, 10);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getEvent()).thenReturn(event);
        when(reservation.getStatus()).thenReturn(ReservationStatus.ACTIVE);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.cancel(reservationId, userId);

        verify(reservation).setStatus(ReservationStatus.CANCELLED);
        verify(reservationRepository).save(reservation);
        verify(eventRepository).save(event);
        assertEquals(6, event.getAvailableSeats());
    }

    @Test
    void shouldThrowWhenCancellingAnotherUsersReservation() {
        Long ownerUserId = 1L;
        Long requestingUserId = 2L;
        Long reservationId = 100L;

        User owner = mock(User.class);
        when(owner.getId()).thenReturn(ownerUserId);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(owner);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThrows(AccessDeniedException.class,
                () -> reservationService.cancel(reservationId, requestingUserId));

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCancellingNonExistentReservation() {
        Long userId = 1L;
        Long reservationId = 999L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.cancel(reservationId, userId));
    }

    @Test
    void shouldSkipPersistenceWhenAlreadyCancelled() {
        Long userId = 1L;
        Long reservationId = 100L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Reservation reservation = mock(Reservation.class);
        when(reservation.getUser()).thenReturn(user);
        when(reservation.getStatus()).thenReturn(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.cancel(reservationId, userId);

        verify(reservationRepository, never()).save(any());
        verifyNoInteractions(eventRepository);
    }

    @Test
    void shouldReturnAllReservationsForUser() {
        Long userId = 1L;
        Long eventId = 10L;

        Event event = buildEvent(eventId, 5, 10);

        Reservation mockReservation = mock(Reservation.class);
        when(mockReservation.getId()).thenReturn(100L);
        when(mockReservation.getEvent()).thenReturn(event);
        when(mockReservation.getStatus()).thenReturn(ReservationStatus.ACTIVE);
        when(mockReservation.getReservationDate()).thenReturn(LocalDateTime.now());

        when(reservationRepository.findAllByUserIdOrderByReservationDateDesc(userId))
                .thenReturn(List.of(mockReservation));

        List<ReservationResponse> responses = reservationService.findAllByUserId(userId);

        assertEquals(1, responses.size());
        assertEquals(100L, responses.get(0).getId());
        assertEquals(eventId, responses.get(0).getEventId());
        assertEquals(ReservationStatus.ACTIVE, responses.get(0).getStatus());
    }
}