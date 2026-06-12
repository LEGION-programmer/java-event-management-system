package pl.eventmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.eventmanager.dto.ReservationResponse;
import pl.eventmanager.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{eventId}")
    public ReservationResponse reserve(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        return reservationService.reserveEvent(eventId, authentication.getName());
    }

    @GetMapping("/my")
    public List<ReservationResponse> myReservations(Authentication authentication) {
        return reservationService.getUserReservations(authentication.getName());
    }

    @DeleteMapping("/{reservationId}")
    public void cancel(
            @PathVariable Long reservationId,
            Authentication authentication
    ) {
        reservationService.cancelReservation(reservationId, authentication.getName());
    }
}