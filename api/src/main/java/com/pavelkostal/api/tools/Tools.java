package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.Position;

import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static boolean isValidGps(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) return false;
        if (longitude < -180 || longitude > 180) return false;
        return true;
    }
    
    public static boolean isValidImage(String image) {
        String[] strings = image.split(",");
        String extension = switch (strings[0]) {//check image's extension
            case "data:image/jpeg;base64" -> "jpeg";
            case "data:image/jpg;base64" -> "jpg";
            case "data:image/png;base64" -> "png";
            default ->//should write cases for more images types
                    null;
        };
        return extension != null;
    }

    public static Photo replaceSpaceWithUnderscore(Photo photo) {
        Position position = photo.getPosition();
        String city = position.getCity();
        position.setCity(city.replace(" ", "_"));
        photo.setPosition(position);
        return photo;
    }

    public static String replaceSpaceWithUnderscore(String text) {
        return text.replace(" ", "_");
    }

    public static List<Photo> replaceUnderscoreWithSpace(List<Photo> listOfPhotos) {
        List<Photo> newList = new ArrayList<>();
        for (Photo photo : listOfPhotos) {
            Position position = photo.getPosition();
            String city = position.getCity();
            city = city.replace("_", " ");
            position.setCity(city);
            photo.setPosition(position);
            newList.add(photo);
        }

        return newList;
    }

    public static List<String> replaceUnderscoreWithSpaceForString(List<String> listOfCity) {
        List<String> newList = new ArrayList<>();
        for (String city : listOfCity) {
            newList.add(city.replace("_", " "));
        }

        return newList;
    }
}
