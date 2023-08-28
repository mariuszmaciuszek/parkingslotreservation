CREATE TABLE IF NOT EXISTS outbox(
    id              UUID            PRIMARY KEY,
    reservation_id  UUID            NOT NULL,
    event_type      VARCHAR(50)     NOT NULL,
    processed       VARCHAR(15)     NOT NULL,
    created_date    TIMESTAMP       NOT NULL,

    CONSTRAINT outbox_unique_reservation_id_event_type_constraint UNIQUE(id,reservation_id)
);

    CREATE INDEX IF NOT EXISTS outbox_reservation_id_index ON outbox(reservation_id);
    CREATE INDEX IF NOT EXISTS outbox_reservation_id_index ON outbox USING HASH(processed);
    CREATE INDEX IF NOT EXISTS outbox_event_type_index_hash ON outbox USING HASH(event_type);

