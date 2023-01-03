package com.pavelkostal.api.apiController;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.pavelkostal.api.constants.ResponseMessages;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.model.ResponsePhotoSaved;
import com.pavelkostal.api.service.PhotoService;
import com.pavelkostal.api.tools.TokenTool;
import com.pavelkostal.api.tools.Tools;
import com.pavelkostal.api.tools.GPSPositionTools;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("api/v1/data")
@AllArgsConstructor
@CrossOrigin()
public class ApiController {

    private final PhotoService photoService;
    private final TokenTool tokenTool;
    private final GPSPositionTools gpsPositionTools;
    
    @GetMapping
    public String hello() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return "Hello from Spring Boot Application Photo Game (" + now.format(dateTimeFormatter) +")";
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<ResponsePhoto> saveImage(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody Photo photo) throws BadJOSEException, ParseException, JOSEException {
    
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        photo.setUniqueUserId(uniqueUserId);

        if (photo.getPosition() == null) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.INVALID_GPS.toString()), HttpStatus.BAD_REQUEST);
        }
    
        if (!Tools.isValidGps(photo.getPosition().getGpsPositionLatitude(), photo.getPosition().getGpsPositionLongitude())) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.NO_GPS.toString()), HttpStatus.BAD_REQUEST);
        }
        
        if (!Tools.isValidImage(photo.getPhotoAsString())) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.INVALID_IMAGE.toString()), HttpStatus.BAD_REQUEST);
        }

        if (!gpsPositionTools.isValidGPSPositionAtEnteredCity(photo)) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.INVALID_GPS_AT_CITY.toString()), HttpStatus.BAD_REQUEST);
        }
        
        long savePhotoId = photoService.savePhoto(photo);
        ResponsePhotoSaved response = new ResponsePhotoSaved(savePhotoId, ResponseMessages.PHOTO_SAVED.toString());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/image/{imageId}")
    public ResponseEntity<ResponsePhoto> getImageById(@PathVariable Long imageId) {
        Optional<Photo> photoById = photoService.getPhotoById(imageId);
        
        if (photoById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(photoById.get(), HttpStatus.OK);
    }
    
    @GetMapping("/images")
    public ResponseEntity<List<Photo>> getAllImagesForCurrentUser(@RequestHeader("Authorization") String bearerToken) throws BadJOSEException, ParseException, JOSEException {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        List<Photo> allImagesForUser = photoService.getAllImagesForSelectedUser(uniqueUserId);
    
        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    @GetMapping("/images/{city}")
    public ResponseEntity<List<Photo>> getAllImagesByCity(@PathVariable String city) {
        List<Photo> allImagesForUser = photoService.getAllPhotosByCity(city);

        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    @GetMapping("/list-of-cities/")
    public ResponseEntity<List<String>> getAllCityInDb() {
        List<String> allCityInDb = photoService.getAllCityInDb();
        return new ResponseEntity<>(Tools.replaceUnderscoreWithSpaceForString(allCityInDb), HttpStatus.OK);
    }
    
}
