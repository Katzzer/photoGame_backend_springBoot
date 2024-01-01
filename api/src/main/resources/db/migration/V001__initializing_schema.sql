CREATE TABLE IF NOT EXISTS photo (
    id BIGSERIAL PRIMARY KEY ,
    gps_position_latitude BIGINT NOT NULL,
    gps_position_longitude BIGINT NOT NULL,
    city TEXT,
    region TEXT,
    locality TEXT,
    country TEXT,
    continent TEXT
);

CREATE TABLE IF NOT EXISTS user_information (
    id BIGSERIAL PRIMARY KEY ,
    unique_user_id TEXT,
    photo_id BIGINT REFERENCES photo(id)
);

INSERT INTO user_information (unique_user_id, photo_id) VALUES ('123', 1)
