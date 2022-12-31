package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.repository.PositionRepository;
import com.pavelkostal.api.tools.Tools;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PositionRepository positionRepository;

    public long savePhoto(Photo photo) {
        Photo photoWithCorrectCityName = Tools.replaceSpaceWithUnderscore(photo);

        Photo savedPhoto = photoRepository.save(photoWithCorrectCityName);
        return savedPhoto.getId();
    }
    
    public Optional<Photo> getPhotoById(long imageId) {
        return photoRepository.findById(imageId);
    }
    
    public List<Photo> getAllImagesForSelectedUser(String uniqueUserId) {
        return photoRepository.findPhotosByUniqueUserId(uniqueUserId);
    }

    public List<Photo> getAllPhotosByCity(String city) {
        city = Tools.replaceSpaceWithUnderscore(city);
        List<Photo> photosByCity = photoRepository.findByCity(city);
        return Tools.replaceUnderscoreWithSpace(photosByCity);
    }

    public List<String> getAllCityInDb() {
        List<String> allCity = positionRepository.getAllCity();
        return allCity.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
