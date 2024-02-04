CREATE TABLE IF NOT EXISTS photo (
    id BIGSERIAL PRIMARY KEY,
    photo_owner TEXT NOT NULL,
    gps_position_latitude NUMERIC(10,8) NOT NULL,
    gps_position_longitude NUMERIC(11,8) NOT NULL,
    city TEXT NOT NULL,
    region TEXT,
    locality TEXT,
    country TEXT,
    continent TEXT
);
