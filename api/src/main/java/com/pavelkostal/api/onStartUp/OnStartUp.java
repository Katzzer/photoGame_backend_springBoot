package com.pavelkostal.api.onStartUp;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OnStartUp {

    private final PhotoRepository photoRepository;

    @Bean
    @Profile("test")
    public void insertDataToDatabaseForTest() {
        Photo photo1FromPrague = new Photo("123", 50.073658, 14.418540, "Prague", null, null, "Czech republic", null);
        Photo photo2FromPrague = new Photo("123", 50.073658, 14.418540, "Prague", null, null, "Czech republic", null);
        Photo photo3FromHk = new Photo("123", 50.20923, 15.83277, "Hradec Kralove", null, null, "Czech republic", null);
        List<Photo> listOfPhotos = List.of(photo1FromPrague, photo2FromPrague, photo3FromHk);

        photoRepository.saveAll(listOfPhotos);
    }
}
