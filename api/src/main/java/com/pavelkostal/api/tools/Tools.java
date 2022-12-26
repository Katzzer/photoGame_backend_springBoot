package com.pavelkostal.api.tools;

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
}
