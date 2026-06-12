package pl.eventmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationAlreadyActiveException extends RuntimeException {

    public ReservationAlreadyActiveException(Long eventId) {
        super("Active reservation already exists for event id: " + eventId);
    }
}