package com.pavelkostal.api.apiController;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.pavelkostal.api.constants.ResponseMessages;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.User;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.model.ResponsePhotoSaved;
import com.pavelkostal.api.service.PhotoService;
import com.pavelkostal.api.tools.TokenTool;
import com.pavelkostal.api.tools.Tools;
import com.pavelkostal.api.tools.GPSPositionTools;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    @PostMapping
    public ResponseEntity<ResponsePhoto> saveImage(
            @RequestHeader("Authorization") String bearerToken,
            @RequestPart("imageFile") MultipartFile multipartFile,
            @RequestPart("photo") User user
    ) throws IOException, BadJOSEException, ParseException, JOSEException {

        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        user.setUniqueUserId(uniqueUserId);

        if (user.getPhoto() == null) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.INVALID_GPS.toString()), HttpStatus.BAD_REQUEST);
        }

        if (!Tools.isValidGps(user.getPhoto().getGpsPositionLatitude(), user.getPhoto().getGpsPositionLongitude())) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.NO_GPS.toString()), HttpStatus.BAD_REQUEST);
        }

        if (!gpsPositionTools.isValidGPSPositionAtEnteredCity(user)) {
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.INVALID_GPS_AT_CITY.toString()), HttpStatus.BAD_REQUEST);
        }

        long savedPhotoId = photoService.savePhoto(user);

        InputStream initialStream = multipartFile.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        @SuppressWarnings("unused")
        int read = initialStream.read(buffer);// without this line of code photo are not saved correctly, DO NOT REMOVE

        String imageName = savedPhotoId + ".jpeg";
        File targetFile = new File("r:\\" + imageName);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }

        ResponsePhotoSaved response = new ResponsePhotoSaved(savedPhotoId, ResponseMessages.PHOTO_SAVED.toString());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("imageId") Long imageId) throws IOException {
        Optional<User> photoById = photoService.getPhotoById(imageId);
        // TODO: check if user has access to this photo

        if (photoById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String imageName = imageId + ".jpeg";
        byte[] imageAsBytes = Files.readAllBytes(Paths.get("R:\\" + imageName));

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//                .eTag(version)
                .body(imageAsBytes);
    }
    
    @GetMapping("/images")
    public ResponseEntity<List<User>> getAllImagesForCurrentUser(@RequestHeader("Authorization") String bearerToken) throws BadJOSEException, ParseException, JOSEException {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        List<User> allImagesForUser = photoService.getAllImagesForSelectedUser(uniqueUserId);
    
        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    @GetMapping("/images/{city}")
    public ResponseEntity<List<Photo>> getAllImagesByCity(@PathVariable String city, @RequestHeader("Authorization") String bearerToken) throws BadJOSEException, ParseException, JOSEException {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        List<Photo> allImagesForUser = photoService.getAllPhotosByCity(city);

        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    @GetMapping("/list-of-cities/")
    public ResponseEntity<List<String>> getAllCityInDb() {
        List<String> allCityInDb = photoService.getAllCityInDb();
        return new ResponseEntity<>(Tools.replaceUnderscoreWithSpaceForString(allCityInDb), HttpStatus.OK);
    }
    
}
