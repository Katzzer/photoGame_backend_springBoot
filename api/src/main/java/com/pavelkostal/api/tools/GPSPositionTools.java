package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.externalApiCalls.PositionStack;
import com.pavelkostal.api.model.PositionStackResponseDataValues;
import com.pavelkostal.api.model.PositionStackResponseDataWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GPSPositionTools {

    private final PositionStack positionStack;

    @Value("${position.stack.access.key}")
    String positionStackAccessKey;

    public static boolean isValidGps(Double latitude, Double longitude) {
        if (latitude == null & latitude == null) return true;
        if (latitude < -90 || latitude > 90) return false;
        return longitude >= -180 && longitude <= 180;
    }

//    public boolean isValidGPSPositionAtEnteredCity(Photo photo) {
//        if (photo.getCity() == null) return true;
//
//        PositionStackResponseDataWrapper data;
//
//        try {
//            data = positionStack.getDataByCity(positionStackAccessKey, photo.getCity());
//        } catch (Exception e) {
//            logger.info("Exception in positionStack: " + e);
//            return false;
//        }
//
//        for (PositionStackResponseDataValues values : data.data()) {
//            double latitudeOfEnteredCity = values.latitude();
//            double longitudeOfEnteredCity = values.longitude();
//            double latitudeFromPhoto = photo.getGpsPositionLatitude();
//            double longitudeFromPhoto = photo.getGpsPositionLongitude();
//
//            double distance = getDistanceBetweenTwoGpsPoints(latitudeOfEnteredCity, latitudeFromPhoto, longitudeOfEnteredCity, longitudeFromPhoto);
//            if (distance < 50) {
//                return true;
//            }
//        }
//
//        return false;
//    }
    
    public void setPositionInformationFromGpsOrCityToCurrentPhoto(Photo photo) {
        String query;
        PositionStackResponseDataWrapper dataByGpsOrCity;
        if (photo.getGpsPositionLatitude() != null && photo.getGpsPositionLongitude() != null) {
            query = photo.getGpsPositionLatitude() + ", " + photo.getGpsPositionLongitude();
            photo.setCity(null);
            dataByGpsOrCity = positionStack.getDataByGps(positionStackAccessKey, query);
        } else {
            query = photo.getCity();
            dataByGpsOrCity = positionStack.getDataByCity(positionStackAccessKey, query);
        }

        for (PositionStackResponseDataValues values : dataByGpsOrCity.data()) {
            if (photo.getCity() == null && !values.locality().equals("null")) {
                photo.setCity(values.locality());
            }

            if (photo.getRegion() == null && !values.region().equals("null")) {
                photo.setRegion(values.region());
            }

            if (photo.getLocality() == null && values.locality() != null && !values.locality().equals("null")) {
                photo.setLocality(values.locality());
            }

            if (photo.getCountry() == null && values.country() != null && !values.country().equals("null")) {
                photo.setCountry(values.country());
            }
            
            if (photo.getContinent() == null && values.continent() != null && !values.continent().equals("null")) {
                photo.setContinent(values.continent());
            }

            if (photo.getGpsPositionLatitude() == null) {
                photo.setGpsPositionLatitude(values.latitude());
            }

            if (photo.getGpsPositionLongitude() == null) {
                photo.setGpsPositionLongitude(values.longitude());
            }

        }
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * {@code @returns} Distance in Kilometers
     */
    private static double getDistanceBetweenTwoGpsPoints(double lat1, double lat2, double lon1, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist * 60 * 1.1515 * 1.609344;
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
}
