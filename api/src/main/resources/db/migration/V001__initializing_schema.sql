CREATE TABLE IF NOT EXISTS photo (
    id BIGSERIAL PRIMARY KEY,
    photo_owner TEXT NOT NULL,
    gps_position_latitude BIGINT NOT NULL,
    gps_position_longitude BIGINT NOT NULL,
    city TEXT,
    region TEXT,
    locality TEXT,
    country TEXT,
    continent TEXT
);

INSERT INTO photo (photo_owner, gps_position_latitude, gps_position_longitude, city, region, locality, country, continent) VALUES ('123', 50.073658, 14.418540, 'Prague', null, null, null, null);
INSERT INTO photo (photo_owner, gps_position_latitude, gps_position_longitude, city, region, locality, country, continent) VALUES ('123', 50.073658, 14.418540, 'Prague', null, null, null, null);
INSERT INTO photo (photo_owner, gps_position_latitude, gps_position_longitude, city, region, locality, country, continent) VALUES ('123', 50.20923, 15.83277, 'Hradec Kralove', null, null, null, null);
