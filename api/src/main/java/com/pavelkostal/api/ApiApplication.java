package com.pavelkostal.api;

import com.github.javafaker.Faker;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.Position;
import com.pavelkostal.api.repository.PhotoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PhotoRepository photoRepository) {
        return args -> {
            Faker faker = new Faker();

            List<Photo> listOfPhotos = new ArrayList<>();

            for (int i = 0; i < 1_000; i++) {
                String city = faker.address().city();
                double longitude = Double.parseDouble(faker.address().longitude().replace(",", "."));
                double latitude = Double.parseDouble(faker.address().latitude().replace(",", "."));
                String country = faker.address().country();

                String uuid = UUID.randomUUID().toString();

                Position position = new Position(longitude, latitude, city, "", "", country, "");
                Photo photo = new Photo("aaa", uuid, position);
                listOfPhotos.add(photo);
            }
            
            photoRepository.saveAll(listOfPhotos);
        };
    }
}
