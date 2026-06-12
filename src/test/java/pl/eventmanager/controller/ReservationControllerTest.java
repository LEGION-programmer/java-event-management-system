package pl.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.eventmanager.dto.ReservationResponse;
import pl.eventmanager.enums.ReservationStatus;
import pl.eventmanager.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock private ReservationService reservationService;
    @InjectMocks private ReservationController reservationController;

    @Test
    void shouldBookReservation() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");

        ReservationResponse response = new ReservationResponse(
                1L, 1L, "Test", LocalDateTime.now(),
                "Warsaw", ReservationStatus.ACTIVE, LocalDateTime.now()
        );
        when(reservationService.book(1L, 1L)).thenReturn(response);

        ResponseEntity<ReservationResponse> result = reservationController.book(1L, jwt);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldCancelReservation() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");

        ResponseEntity<Void> result = reservationController.cancel(1L, jwt);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(reservationService).cancel(1L, 1L);
    }

    @Test
    void shouldReturnMyReservations() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("1");
        when(reservationService.findAllByUserId(1L)).thenReturn(List.of());

        List<ReservationResponse> result = reservationController.myReservations(jwt);

        assertNotNull(result);
    }
}