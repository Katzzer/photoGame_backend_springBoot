package com.pavelkostal.api.model;

public record PositionStackResponseDataValues(
        double latitude,
        double longitude,
        String type,
        String name,
//        String number,
//        String postal_code,
//        String street,
        double confidence,
        String region,
        String region_code,
        String locality,
        String administrative_area,
        String neighbourhood,
        String country,
        String country_code,
        String continent,
        String label
) {
}
