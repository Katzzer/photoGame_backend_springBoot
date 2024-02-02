package com.pavelkostal.api.apiController;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.service.PhotoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController()
@RequestMapping("/${base-controller.path}")
@AllArgsConstructor
@CrossOrigin()
@Slf4j
public class ApiController {

    private final PhotoService photoService;

    @GetMapping
    public String hello() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return "Hello from Spring Boot Application Photo Game (" + now.format(dateTimeFormatter) +")";
    }

    @PostMapping("/save-photo")
    public ResponseEntity<ResponsePhoto> savePhoto(
            @RequestHeader("Authorization") String bearerToken,
            @RequestPart("imageFile") MultipartFile multipartFile,
            @RequestPart("photo") Photo photo
    ) throws BadJOSEException, ParseException, JOSEException {
        return photoService.savePhoto(bearerToken, multipartFile, photo);
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("photoId") Long imageId) throws IOException {
       return photoService.getPhotoById(imageId, false);
    }

    @GetMapping("/photo/thumbnail/{imageId}")
    public ResponseEntity<byte[]> getImageThumbnailById(@PathVariable("imageId") Long imageId) throws IOException {
        return photoService.getPhotoById(imageId, true);
    }
    
    @GetMapping("/photos/{city}")
    public ResponseEntity<List<Photo>> getAllPhotosByCity(@PathVariable("city") String city) {
        return photoService.getAllPhotosByCity(city);
    }

    @GetMapping("/photos/all-photos-for-current-user")
    public ResponseEntity<List<Photo>> getAllImagesForSelectedUser(@RequestHeader("Authorization") String bearerToken) {
        return photoService.getAllImagesForSelectedUser(bearerToken);
    }

    @GetMapping("/list-of-cities")
    public ResponseEntity<List<String>> getAllCityInDb() {
       return photoService.getAllCityInDb();
    }
    
}
