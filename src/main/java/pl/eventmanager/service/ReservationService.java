package pl.eventmanager.service;

import pl.eventmanager.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse reserveEvent(
            Long eventId,
            String username
    );

    List<ReservationResponse> getUserReservations(
            String username
    );

    void cancelReservation(
            Long reservationId,
            String username
    );
}