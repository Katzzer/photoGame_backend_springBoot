package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
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

    public long savePhoto(Photo photo) {
        photo.setCity(Tools.replaceSpaceWithUnderscore(photo.getCity()));
        Photo savedPhoto = photoRepository.save(photo);
        return savedPhoto.getId();
    }
    
    public Optional<Photo> getPhotoById(long imageId) {
        return photoRepository.findById(imageId);
    }
    
    public List<Photo> getAllImagesForSelectedUser(String uniqueUserId) {
        return photoRepository.findPhotosByUniqueUserId(uniqueUserId);
    }

    public List<Photo> getAllImagesByCity(String city) {
        city = Tools.replaceSpaceWithUnderscore(city);
        List<Photo> photosByCity = photoRepository.findPhotosByCity(city);
        return Tools.replaceUnderscoreWithSpace(photosByCity);
    }

    public List<String> getAllCityInDb() {
        List<String> allCity = photoRepository.getAllCity();
        return allCity.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
