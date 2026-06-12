package pl.eventmanager.dto;

import java.time.LocalDateTime;

public class TicketResponse {

    private Long reservationId;
    private String ticketNumber;
    private String eventTitle;
    private String eventLocation;
    private LocalDateTime eventDate;
    private String holderName;
    private String message;
    private LocalDateTime issuedAt;

    public TicketResponse() {
    }

    public TicketResponse(
            Long reservationId,
            String ticketNumber,
            String eventTitle,
            String eventLocation,
            LocalDateTime eventDate,
            String holderName,
            String message,
            LocalDateTime issuedAt
    ) {
        this.reservationId = reservationId;
        this.ticketNumber = ticketNumber;
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.holderName = holderName;
        this.message = message;
        this.issuedAt = issuedAt;
    }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
}