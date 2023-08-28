CREATE TABLE IF NOT EXISTS user_cars(
    id              UUID                                PRIMARY KEY,
    user_id         UUID                                NOT NULL,
    plate           VARCHAR(10)                         NOT NULL,
    car_size        VARCHAR(30)                         NOT NULL,
    created_date    TIMESTAMP                           NOT NULL,
    updated_date    TIMESTAMP                           NOT NULL,

    CONSTRAINT user_cars_unique_plate_constraint UNIQUE(plate),

    CONSTRAINT foreign_key_users FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
    );

    CREATE INDEX IF NOT EXISTS users_id_index ON user_cars(user_id);
