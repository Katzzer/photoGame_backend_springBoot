package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    public long savePhoto(Photo photo) {
        Photo savedPhoto = photoRepository.save(photo);
        return savedPhoto.getId();
    }
}
