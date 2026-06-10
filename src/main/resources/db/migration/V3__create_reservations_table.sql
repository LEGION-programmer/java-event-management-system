CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,

                              user_id BIGINT NOT NULL,
                              event_id BIGINT NOT NULL,

                              status VARCHAR(30) NOT NULL,

                              reservation_date TIMESTAMP NOT NULL,

                              CONSTRAINT fk_reservation_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id),

                              CONSTRAINT fk_reservation_event
                                  FOREIGN KEY (event_id)
                                      REFERENCES events(id)
);