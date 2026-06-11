ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE events
    ALTER COLUMN created_by SET NOT NULL;

ALTER TABLE events
    ADD CONSTRAINT chk_events_capacity_positive
        CHECK (capacity > 0);

ALTER TABLE events
    ADD CONSTRAINT chk_events_available_seats_range
        CHECK (
            available_seats >= 0
                AND available_seats <= capacity
            );

CREATE UNIQUE INDEX uk_active_reservation_user_event
    ON reservations (user_id, event_id)
    WHERE status = 'ACTIVE';

CREATE INDEX idx_events_event_date
    ON events (event_date);

CREATE INDEX idx_reservations_user_id
    ON reservations (user_id);

CREATE INDEX idx_reservations_event_id
    ON reservations (event_id);