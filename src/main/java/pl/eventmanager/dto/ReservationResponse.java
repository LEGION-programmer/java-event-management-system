package pl.eventmanager.dto;

import java.time.LocalDateTime;

public class ReservationResponse {

    private Long reservationId;
    private Long eventId;
    private String eventTitle;
    private LocalDateTime reservationDate;
    private String status;

    public ReservationResponse() {
    }

    public ReservationResponse(
            Long reservationId,
            Long eventId,
            String eventTitle,
            LocalDateTime reservationDate,
            String status
    ) {
        this.reservationId = reservationId;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}