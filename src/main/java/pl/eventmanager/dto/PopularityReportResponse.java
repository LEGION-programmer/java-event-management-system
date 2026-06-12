package pl.eventmanager.dto;

import java.time.LocalDateTime;

public class PopularityReportResponse {

    private Long eventId;
    private String eventTitle;
    private LocalDateTime eventDate;
    private String location;
    private int capacity;
    private long activeReservations;
    private double occupancyPercent;

    public PopularityReportResponse() {
    }

    public PopularityReportResponse(
            Long eventId,
            String eventTitle,
            LocalDateTime eventDate,
            String location,
            int capacity,
            long activeReservations,
            double occupancyPercent
    ) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.location = location;
        this.capacity = capacity;
        this.activeReservations = activeReservations;
        this.occupancyPercent = occupancyPercent;
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public long getActiveReservations() { return activeReservations; }
    public void setActiveReservations(long activeReservations) { this.activeReservations = activeReservations; }

    public double getOccupancyPercent() { return occupancyPercent; }
    public void setOccupancyPercent(double occupancyPercent) { this.occupancyPercent = occupancyPercent; }
}