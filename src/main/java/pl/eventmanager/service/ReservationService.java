package pl.eventmanager.service;

import pl.eventmanager.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse book(Long userId, Long eventId);

    void cancel(Long reservationId, Long userId);

    List<ReservationResponse> findAllByUserId(Long userId);
}