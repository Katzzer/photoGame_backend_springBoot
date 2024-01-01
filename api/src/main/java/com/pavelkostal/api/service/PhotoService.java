package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.User;
import com.pavelkostal.api.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final GPSPositionTools gpsPositionTools;

    public long savePhoto(User user) {
        user.setPhoto(gpsPositionTools.getPositionInformationFromGps(user));

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
    
    public Optional<User> getPhotoById(long imageId) {
        return userRepository.findById(imageId);
    }
    
    public List<User> getAllImagesForSelectedUser(String uniqueUserId) {
        return userRepository.findPhotosByUniqueUserId(uniqueUserId);
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
