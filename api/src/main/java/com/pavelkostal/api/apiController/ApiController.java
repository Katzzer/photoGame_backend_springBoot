package com.pavelkostal.api.apiController;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.service.PhotoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    ) {
        return photoService.savePhoto(bearerToken, multipartFile, photo);
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable("photoId") Long imageId) {
       return photoService.getPhotoById(imageId);
    }

    @GetMapping("/photo-for-mobile/{photoId}")
    public ResponseEntity<byte[]> getPhotoForMobileById(@PathVariable("photoId") Long photoId)  {
        return photoService.getMobilePhotoById(photoId);
    }

    @GetMapping("/photo/thumbnail/{photoId}")
    public ResponseEntity<byte[]> getImageThumbnailById(@PathVariable("photoId") Long photoId) {
        return photoService.getThumbnailPhotoById(photoId);
    }

    @GetMapping("/photos/all-photos-for-current-user")
    public ResponseEntity<List<Photo>> getAllImagesForSelectedUser(@RequestHeader("Authorization") String bearerToken) {
        return photoService.getAllPhotosForSelectedUser(bearerToken);
    }

    @GetMapping("/find-photos-by-location")
    public ResponseEntity<List<String>> findAllCountries() {
        return photoService.findAllCountries();
    }

    @GetMapping("/find-photos-by-location/{country}")
    public ResponseEntity<List<String>> findAllCityByCountry(@PathVariable("country") String country) {
        return photoService.findAllCityByCountry(country);
    }

    @GetMapping("/find-photos-by-location/{country}/{city}")
    public ResponseEntity<List<Photo>> findAllPhotosByCountryAndCity(@PathVariable("country") String country, @PathVariable("city") String city) {
        return photoService.findAllPhotosByCountryAndCity(country, city);
    }

}
