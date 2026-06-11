package pl.eventmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SeatUnavailableException extends RuntimeException {

    public SeatUnavailableException() {
        super("No available seats for this event");
    }
}