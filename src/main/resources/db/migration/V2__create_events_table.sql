CREATE TABLE events (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description VARCHAR(2000),
                        location VARCHAR(255) NOT NULL,
                        event_date TIMESTAMP NOT NULL,
                        capacity INTEGER NOT NULL,
                        available_seats INTEGER NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        created_by BIGINT,

                        CONSTRAINT fk_event_creator
                            FOREIGN KEY (created_by)
                                REFERENCES users(id)
);