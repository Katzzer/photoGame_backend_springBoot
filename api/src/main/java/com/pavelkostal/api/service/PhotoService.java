package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.tools.GPSPositionTools;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final GPSPositionTools gpsPositionTools;

    public long savePhoto(Photo photo) {
        gpsPositionTools.setPositionInformationFromGpsToCurrentPhoto(photo);

        Photo savedUser = photoRepository.save(photo);
        return savedUser.getId();
    }
    
    public Optional<Photo> getPhotoById(long imageId) {
        return photoRepository.findById(imageId);
    }
    
    public List<Photo> getAllImagesForSelectedUser(String uniqueUserId) {
        return photoRepository.findPhotosByUniqueUserId(uniqueUserId);
    }
    public List<Photo> getAllPhotosByCity(String city) {
        return photoRepository.findAllPhotosByCity(city);
    }

    public List<String> getAllCityInDb() {
        List<String> allCity = photoRepository.findAllCity();
        return allCity.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
