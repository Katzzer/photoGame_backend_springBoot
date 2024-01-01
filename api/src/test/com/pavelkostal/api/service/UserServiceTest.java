package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.User;
import com.pavelkostal.api.repository.UserRepository;
import com.pavelkostal.api.repository.PhotoRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoService underTest;

    Photo photo1;
    Photo photo2;
    Photo photo3;
    User testingUser1;
    User testingUser2;
    User testingUser3;

    String cityHK = "Hradec Kralove";
    String cityPrague = "Prague";

    @Before("")
    public void before() {

    }

//    @BeforeAll
//    public void beforeAll() {
//
//    }

    @BeforeEach
    public void beforeEach() {
        photo1 = new Photo(50.2092567, 15.8327564, cityHK, "null", "null", "null", "null");
        photo2 = new Photo(50.2092567, 15.8327564, cityPrague,  "null", "null", "null", "null");
        photo3 = new Photo(50.2092567, 15.8327564, cityHK, "null", "null", "null", "null");
        testingUser1 = new User("123", photo1);
        testingUser2 = new User("123", photo2);
        testingUser3 = new User("1234", photo3);
        userRepository.deleteAll();
        userRepository.save(testingUser1);
        userRepository.save(testingUser2);
        userRepository.save(testingUser3);
    }
    
    @Test
    @Disabled
    void savePhoto() {
        // TODO: implement later
    }

    @Test
    @Disabled
    void getPhotoById() {
        // TODO: implement later
    }

    @Test
    void getAllImagesForSelectedUser() {
        // given
        // when
        List<User> listOfPhotosByUser = underTest.getAllImagesForSelectedUser("123");

        // then
        assertEquals(2, listOfPhotosByUser.size());
    }

    @Test
    void getAllImagesByCity() {
        // given

        // when
        List<Photo> listOfPhotosByCity1 = underTest.getAllPhotosByCity(cityHK);
        List<Photo> listOfPhotosByCity2 = underTest.getAllPhotosByCity(cityPrague);

        // then
        assertEquals(2, listOfPhotosByCity1.size());
        assertEquals(1, listOfPhotosByCity2.size());
    }

    @Test
    void getAllCityInDb() {
        // given
        // when
        List<String> allCityInDb = underTest.getAllCityInDb();

        // then
//        assertEquals(2, allCityInDb.size());
        assertTrue(allCityInDb.contains(cityHK));
        assertTrue(allCityInDb.contains(cityPrague));
    }
}
