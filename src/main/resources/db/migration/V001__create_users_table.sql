CREATE TABLE IF NOT EXISTS users(
    id              UUID            PRIMARY KEY,
    first_name      VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    birth_date      DATE            NOT NULL,
    email           VARCHAR(320)    NOT NULL,
    street_line_1   VARCHAR(100)    NOT NULL,
    street_line_2   VARCHAR(100),
    city            VARCHAR(100)    NOT NULL,
    country         VARCHAR(2)      NOT NULL,
    postcode        VARCHAR(15)     NOT NULL,
    created_date    TIMESTAMP       NOT NULL,
    updated_date    TIMESTAMP       NOT NULL,

    CONSTRAINT users_unique_email_constraint UNIQUE(email)

    );

    CREATE UNIQUE INDEX IF NOT EXISTS users_email_index ON users(email);
