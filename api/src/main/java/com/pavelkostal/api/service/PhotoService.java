package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    public long savePhoto(Photo photo) {
        Photo savedPhoto = photoRepository.save(photo);
        return savedPhoto.getId();
    }
    
    public Optional<Photo> getPhotoById(long imageId) {
        return photoRepository.findById(imageId);
    }
    
    public List<Photo> getAllImagesForSelectedUser(String uniqueUserId) {
        return photoRepository.findPhotosByUniqueUserId(uniqueUserId);
    }
}
