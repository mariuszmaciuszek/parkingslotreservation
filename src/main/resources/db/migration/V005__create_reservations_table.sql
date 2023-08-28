
CREATE TABLE IF NOT EXISTS reservations(
    id                      UUID,
    user_id                 UUID                                                                NOT NULL,
    parking_slot_id         UUID                                                                NOT NULL,
    user_car_id             UUID                                                                NOT NULL,
    state                   VARCHAR(30)                                                         NOT NULL,
    started_date            TIMESTAMP                                                           NOT NULL,
    ended_date              TIMESTAMP                                                           NOT NULL,
    occupation_ended_date   TIMESTAMP,
    created_date            TIMESTAMP                                                           NOT NULL,
    updated_date            TIMESTAMP                                                           NOT NULL,

    PRIMARY KEY (id, state),
    CONSTRAINT foreign_key_users FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
    )PARTITION BY LIST(state);

    CREATE INDEX IF NOT EXISTS reservations_state_hash_index ON reservations USING HASH(state);
    CREATE INDEX IF NOT EXISTS user_id_index ON reservations(user_id);
    CREATE INDEX IF NOT EXISTS reservations_created_date_brin_index ON reservations USING BRIN(created_date);
    CREATE INDEX IF NOT EXISTS reservations_started_date_brin_index ON reservations USING BRIN(started_date);
    CREATE INDEX IF NOT EXISTS reservations_ended_date_brin_index ON reservations USING BRIN(ended_date);

    CREATE TABLE reserved_reservations PARTITION OF reservations FOR VALUES IN ('RESERVED');
    CREATE TABLE in_use_reservations PARTITION OF reservations FOR VALUES IN ('IN_USE');
    CREATE TABLE released_reservations PARTITION OF reservations FOR VALUES IN ('RELEASED');
    CREATE TABLE canceled_reservations PARTITION OF reservations FOR VALUES IN ('CANCELED');
    CREATE TABLE occupied_reservations PARTITION OF reservations FOR VALUES IN ('OCCUPIED');

