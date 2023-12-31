CREATE TABLE IF NOT EXISTS position (
    id BIGSERIAL PRIMARY KEY ,
    gps_position_latitude BIGINT NOT NULL,
    gps_position_longitude BIGINT NOT NULL,
    city TEXT,
    region TEXT,
    locality TEXT,
    country TEXT,
    continent TEXT
);

CREATE TABLE IF NOT EXISTS photo (
    id BIGSERIAL PRIMARY KEY ,
    unique_user_id TEXT,
    position_id BIGINT REFERENCES position(id)
);
