package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.Position;
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

    public boolean isValidGPSPositionAtEnteredCity(Photo photo) {
        PositionStackResponseDataWrapper data;

        try {
            data = positionStack.getDataByCity(positionStackAccessKey, photo.getPosition().getCity());
        } catch (Exception e) {
            return false;
        }

        for (PositionStackResponseDataValues values : data.data()) {
            double latitudeOfEnteredCity = values.latitude();
            double longitudeOfEnteredCity = values.longitude();
            double latitudeFromPhoto = photo.getPosition().getGpsPositionLatitude();
            double longitudeFromPhoto = photo.getPosition().getGpsPositionLongitude();

            double distance = distance(latitudeOfEnteredCity, latitudeFromPhoto, longitudeOfEnteredCity, longitudeFromPhoto, 0, 0);
            if (distance < 50000) {
                return true;
            }
        }

        return false;
    }
    
    public Position getPositionInformationFromGps(Photo photo) {
        String query = photo.getPosition().getGpsPositionLatitude() + ", " + photo.getPosition().getGpsPositionLongitude();

        PositionStackResponseDataWrapper dataByGps = positionStack.getDataByGps(positionStackAccessKey, query);
        String region = "";
        String locality = "";
        String country = "";
        String continent = "";

        for (PositionStackResponseDataValues values : dataByGps.data()) {
            if (region.equals("") && !values.region().equals("null")) {
                region = values.region();
            }

            if (locality.equals("") && !values.locality().equals("null")) {
                locality = values.locality();
            }

            if (country.equals("") && !values.country().equals("null")) {
                country = values.country();
            }
            
            if (continent.equals("") && !values.continent().equals("null")) {
                continent = values.continent();
            }
            
        }
        
        return new Position(photo.getPosition().getGpsPositionLatitude(), photo.getPosition().getGpsPositionLongitude(), photo.getPosition().getCity(), region, locality, country, continent);
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    private static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
