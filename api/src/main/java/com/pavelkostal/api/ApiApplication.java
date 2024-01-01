package com.pavelkostal.api;

import com.github.javafaker.Faker;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.User;
import com.pavelkostal.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Profile({"h2"})
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            Faker faker = new Faker();

            List<User> listOfUsers = new ArrayList<>();

            for (int i = 0; i < 1_000; i++) {
                String city = faker.address().city();
                double longitude = Double.parseDouble(faker.address().longitude().replace(",", "."));
                double latitude = Double.parseDouble(faker.address().latitude().replace(",", "."));
                String country = faker.address().country();

                String uuid = UUID.randomUUID().toString();

                Photo photo = new Photo(longitude, latitude, city, "", "", country, "");
                User user = new User(uuid, photo);
                listOfUsers.add(user);
            }

            userRepository.saveAll(listOfUsers);
        };
    }
}
