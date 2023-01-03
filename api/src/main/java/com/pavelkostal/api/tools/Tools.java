package com.pavelkostal.api.tools;

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

    public static List<String> replaceUnderscoreWithSpaceForString(List<String> listOfCity) {
        List<String> newList = new ArrayList<>();
        for (String city : listOfCity) {
            newList.add(city.replace("_", " "));
        }

        return newList;
    }
    
    public static String replaceSpecialCharactersInString(String text) {
        text = text.replace("ě", "e");
        text = text.replace("š", "s");
        text = text.replace("č", "c");
        text = text.replace("ř", "r");
        text = text.replace("ž", "z");
        text = text.replace("ý", "y");
        text = text.replace("á", "a");
        text = text.replace("í", "i");
        text = text.replace("é", "e");
        text = text.replace("ů", "u");
        text = text.replace("ú", "u");

        text = text.replace("Ě", "E");
        text = text.replace("Š", "S");
        text = text.replace("Č", "C");
        text = text.replace("Ř", "R");
        text = text.replace("Ž", "Z");
        text = text.replace("Ý", "Y");
        text = text.replace("Á", "A");
        text = text.replace("Í", "I");
        text = text.replace("É", "E");
        text = text.replace("Ú", "U");
        
        return text;
    }
}
