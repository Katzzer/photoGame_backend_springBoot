package com.pavelkostal.api.service;

import com.pavelkostal.api.constants.ResponseMessages;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.model.ResponsePhotoSaved;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.tools.GPSPositionTools;
import com.pavelkostal.api.tools.TokenTool;
import com.pavelkostal.api.tools.Tools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final GPSPositionTools gpsPositionTools;
    private final TokenTool tokenTool;
    private final Tools tools;

    @Value("${save-photo-path}")
    private String savePhotoPath;

    public ResponseEntity<ResponsePhoto> savePhoto(String bearerToken, MultipartFile multipartFile, Photo photo) {
        setPhotoOwnerFromToken(photo, bearerToken);

        ResponseEntity<ResponsePhoto> errorResponse = validatePhoto(photo);
        if (errorResponse != null) return errorResponse;

        gpsPositionTools.setPositionInformationFromGpsOrCityToCurrentPhoto(photo);

        Photo savedPhoto = photoRepository.save(photo);

        tools.savePhotoWithThumbnail(multipartFile, savedPhoto.getId());

        return createSuccessResponse(savedPhoto.getId());
    }

    private void setPhotoOwnerFromToken(Photo photo, String bearerToken) {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        photo.setPhotoOwner(uniqueUserId);
    }

    private ResponseEntity<ResponsePhoto> validatePhoto(Photo photo) {
        if (!gpsPositionTools.isValidGps(photo.getGpsPositionLatitude(), photo.getGpsPositionLongitude())) {
            log.warn(ResponseMessages.INVALID_GPS.toString());
            return createErrorResponse(ResponseMessages.INVALID_GPS);
        }

        if (photo.getGpsPositionLatitude() == null && photo.getGpsPositionLongitude() == null && photo.getCity() == null) {
            log.warn(ResponseMessages.NO_GPS_NOR_CITY.toString());
            return createErrorResponse(ResponseMessages.NO_GPS_NOR_CITY);
        }
        return null;
    }

    private ResponseEntity<ResponsePhoto> createErrorResponse(ResponseMessages responseMessage) {
        return new ResponseEntity<>(new ResponsePhotoSaved(null, responseMessage.toString()), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ResponsePhoto> createSuccessResponse(long savedPhotoId) {
        ResponsePhotoSaved response = new ResponsePhotoSaved(savedPhotoId, ResponseMessages.PHOTO_SAVED.toString());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<byte[]> getPhotoById(long imageId) {
        return getPhotoById(imageId, false, false);
    }

    public ResponseEntity<byte[]> getThumbnailPhotoById(long imageId) {
        return getPhotoById(imageId, true, false);
    }

    public ResponseEntity<byte[]> getMobilePhotoById(long imageId) {
        return getPhotoById(imageId, false, true);
    }

    private ResponseEntity<byte[]> getPhotoById(long imageId, boolean getThumbnail, boolean getPhotoForMobile) {
        Optional<Photo> photoById = photoRepository.findById(imageId);
        // TODO: check if user has access to this photo

        if (photoById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String extension = ".jpeg";
        if (getThumbnail) {
            extension = "_thumbnail.jpeg";
        } else if (getPhotoForMobile) {
            extension = "_mobileVersion.jpeg";
        }

        String imageName = imageId + extension;

        byte[] imageAsBytes;
        try {
            imageAsBytes = Files.readAllBytes(Paths.get(savePhotoPath + File.separator + imageName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//                .eTag(version)
                .body(imageAsBytes);
    }

    public ResponseEntity<List<Photo>> getAllPhotosForSelectedUser(String bearerToken) {
        String uniqueUserId = tokenTool.getUniqueUserId(bearerToken);
        List<Photo> allImagesForUser = photoRepository.findPhotosByUniqueUserId(uniqueUserId);
        return new ResponseEntity<>(allImagesForUser, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> findAllCountries() {
        List<String> allCountries = photoRepository.findAllCountries();
        return new ResponseEntity<>(tools.replaceUnderscoreWithSpaceForString(allCountries), HttpStatus.OK);
    }

    public ResponseEntity<List<String>> findAllCityByCountry(String country) {
        List<String> allCountries = photoRepository.findAllCityByCountry(country);
        return new ResponseEntity<>(tools.replaceUnderscoreWithSpaceForString(allCountries), HttpStatus.OK);
    }

    public ResponseEntity<List<Photo>> findAllPhotosByCountryAndCity(String country, String city) {
        List<Photo> allCountries = photoRepository.findAllPhotosByCountryAndCity(country, city);
        return new ResponseEntity<>(allCountries, HttpStatus.OK);
    }
}
