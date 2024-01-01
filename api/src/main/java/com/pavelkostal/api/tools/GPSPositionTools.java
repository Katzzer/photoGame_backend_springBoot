package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.externalApiCalls.PositionStack;
import com.pavelkostal.api.model.PositionStackResponseDataValues;
import com.pavelkostal.api.model.PositionStackResponseDataWrapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GPSPositionTools {

    Logger logger = LoggerFactory.getLogger(GPSPositionTools.class);

    private final PositionStack positionStack;

    @Value("${position.stack.access.key}")
    String positionStackAccessKey;

    public boolean isValidGPSPositionAtEnteredCity(Photo photo) {
        PositionStackResponseDataWrapper data;

        try {
            data = positionStack.getDataByCity(positionStackAccessKey, photo.getCity());
        } catch (Exception e) {
            logger.info("Exception in positionStack: " + e);
            return false;
        }

        for (PositionStackResponseDataValues values : data.data()) {
            double latitudeOfEnteredCity = values.latitude();
            double longitudeOfEnteredCity = values.longitude();
            double latitudeFromPhoto = photo.getGpsPositionLatitude();
            double longitudeFromPhoto = photo.getGpsPositionLongitude();

            double distance = distance(latitudeOfEnteredCity, latitudeFromPhoto, longitudeOfEnteredCity, longitudeFromPhoto);
            if (distance < 50000) {
                return true;
            }
        }

        return false;
    }
    
    public void setPositionInformationFromGpsToCurrentPhoto(Photo photo) {
        String query = photo.getGpsPositionLatitude() + ", " + photo.getGpsPositionLongitude();

        PositionStackResponseDataWrapper dataByGps = positionStack.getDataByGps(positionStackAccessKey, query);

        for (PositionStackResponseDataValues values : dataByGps.data()) {
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
            
        }
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * {@code @returns} Distance in Meters
     */
    private static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist * 60 * 1.1515;
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
}
