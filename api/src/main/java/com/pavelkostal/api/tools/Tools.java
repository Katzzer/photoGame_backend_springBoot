package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;

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

    public static String replaceSpaceWithUnderscore(String text) {
        return text.replace(" ", "_");
    }

    public static List<Photo> replaceUnderscoreWithSpace(List<Photo> listOfPhotos) {
        List<Photo> newList = new ArrayList<>();
        for (Photo photo : listOfPhotos) {
            photo.setCity(photo.getCity().replace("_", " "));
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
