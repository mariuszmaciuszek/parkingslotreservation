CREATE TABLE IF NOT EXISTS parkings(
    id              UUID                         PRIMARY KEY,
    name            VARCHAR(300)                 NOT NULL,
    type            VARCHAR(30)                  NOT NULL,
    street_line_1   VARCHAR(100)                 NOT NULL,
    street_line_2   VARCHAR(100),
    city            VARCHAR(100)                 NOT NULL,
    country         VARCHAR(2)                   NOT NULL,
    postcode        VARCHAR(15)                  NOT NULL,
    created_date    TIMESTAMP                    NOT NULL,
    updated_date    TIMESTAMP                    NOT NULL,

    CONSTRAINT parkings_unique_address_constraint UNIQUE(street_line_1, street_line_2, city, country, postcode)
     );
