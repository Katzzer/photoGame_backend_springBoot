CREATE TABLE IF NOT EXISTS position (
    id BIGSERIAL PRIMARY KEY ,
    gps_position_latitude BIGINT NOT NULL,
    gps_position_longitude BIGINT NOT NULL,
    city TEXT,
    region TEXT NOT NULL,
    locality TEXT NOT NULL,
    country TEXT NOT NULL,
    continent TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS photo (
    id BIGSERIAL PRIMARY KEY ,
    photo TEXT,
    unique_user_id TEXT,
    position_id BIGINT REFERENCES position(id)
);