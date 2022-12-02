package com.pavelkostal.api.tool;

public class Tools {

    public static boolean isValidGps(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) return false;
        if (longitude < -180 || longitude > 180) return false;
        return true;
    }
}
