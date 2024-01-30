package com.pavelkostal.api.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.pavelkostal.api.constants.ResponseMessages;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.model.ResponsePhotoSaved;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.tools.GPSPositionTools;
import com.pavelkostal.api.tools.TokenTool;
import com.pavelkostal.api.tools.Tools;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final GPSPositionTools gpsPositionTools;
    private final TokenTool tokenTool;

    public ResponseEntity<ResponsePhoto> savePhoto(String bearerToken, MultipartFile multipartFile, Photo photo) throws BadJOSEException, ParseException, JOSEException {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        photo.setPhotoOwner(uniqueUserId);

        if (!GPSPositionTools.isValidGps(photo.getGpsPositionLatitude(), photo.getGpsPositionLongitude())) {
            log.warn(ResponseMessages.INVALID_GPS.toString());
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.INVALID_GPS.toString()), HttpStatus.BAD_REQUEST);
        }

        if (photo.getGpsPositionLatitude() == null && photo.getGpsPositionLongitude() == null && photo.getCity() == null) {
            log.warn(ResponseMessages.NO_GPS_NOR_CITY.toString());
            return new ResponseEntity<>(new ResponsePhotoSaved(null, ResponseMessages.NO_GPS_NOR_CITY.toString()), HttpStatus.BAD_REQUEST);
        }

        gpsPositionTools.setPositionInformationFromGpsOrCityToCurrentPhoto(photo);

        Photo savedPhoto = photoRepository.save(photo);
        long savedPhotoId = savedPhoto.getId();

        Tools.savePhotoWithThumbnail(multipartFile, savedPhotoId);

        ResponsePhotoSaved response = new ResponsePhotoSaved(savedPhotoId, ResponseMessages.PHOTO_SAVED.toString());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<byte[]> getPhotoById(long imageId, boolean getThumbnail) throws IOException {
        Optional<Photo> photoById = photoRepository.findById(imageId);
        // TODO: check if user has access to this photo

        if (photoById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String extension = ".jpeg";
        if (getThumbnail) {
            extension = "_thumbnail.jpeg";
        }

        String imageName = imageId + extension;
        byte[] imageAsBytes = Files.readAllBytes(Paths.get("R:\\" + imageName));

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//                .eTag(version)
                .body(imageAsBytes);
    }

    public ResponseEntity<List<Photo>> getAllImagesForSelectedUser(String bearerToken) {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        List<Photo> allImagesForUser = photoRepository.findPhotosByUniqueUserId(uniqueUserId);
        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    public  ResponseEntity<List<Photo>> getAllPhotosByCity(String city) {
        List<Photo> allImagesForUser = photoRepository.findAllPhotosByCity(city);
        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getAllCityInDb() {
        List<String> allCityInDb = photoRepository.findAllCity();
        List<String> uniqueCities = allCityInDb.stream()
                .distinct()
                .toList();

        return new ResponseEntity<>(Tools.replaceUnderscoreWithSpaceForString(uniqueCities), HttpStatus.OK);
    }
}
