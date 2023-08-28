
CREATE TABLE IF NOT EXISTS parking_slots(
    id              UUID                                PRIMARY KEY,
    parking_id      UUID                                NOT NULL,
    size            VARCHAR(30)                         NOT NULL,
    availability    VARCHAR(30)                         NOT NULL,
    created_date    TIMESTAMP                           NOT NULL,
    updated_date    TIMESTAMP                           NOT NULL,

    CONSTRAINT foreign_key_parkings FOREIGN KEY (parking_id) REFERENCES parkings(id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
    );

    CREATE INDEX IF NOT EXISTS parking_slots_size_hash_index ON parking_slots USING HASH(size);
    CREATE INDEX IF NOT EXISTS parking_slots_availability_hash_index ON parking_slots USING HASH(availability);
    CREATE INDEX IF NOT EXISTS parking_id_index ON parking_slots(parking_id);
