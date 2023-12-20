package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.Position;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.repository.PositionRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PhotoServiceTest {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    PhotoService underTest;

    Position position1;
    Position position2;
    Position position3;
    Photo testingPhoto1;
    Photo testingPhoto2;
    Photo testingPhoto3;

    @Before("")
    public void before() {

    }
    
    @BeforeEach
    public void beforeEach() {
        position1 = new Position(50.2092567, 15.8327564, "Hradec_Kralove", "null", "null", "null", "null");
        position2 = new Position(50.2092567, 15.8327564, "Prag",  "null", "null", "null", "null");
        position3 = new Position(50.2092567, 15.8327564, "Hradec_Kralove", "null", "null", "null", "null");
        testingPhoto1 = new Photo("data:image/jpeg;base64,someValue", "123", position1);
        testingPhoto2 = new Photo("data:image/jpeg;base64,someValue", "123", position2);
        testingPhoto3 = new Photo("data:image/jpeg;base64,someValue", "1234", position3);
        photoRepository.save(testingPhoto1);
        photoRepository.save(testingPhoto2);
        photoRepository.save(testingPhoto3);
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
        List<Photo> listOfPhotosByUser = underTest.getAllImagesForSelectedUser("123");

        // then
        assertEquals(2, listOfPhotosByUser.size());
    }

    @Test
    void getAllImagesByCity() {
        // given
        // when
        List<Photo> listOfPhotosByCity1 = underTest.getAllPhotosByCity("Hradec_Kralove");
        List<Photo> listOfPhotosByCity2 = underTest.getAllPhotosByCity("Prag");

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
        assertEquals(2, allCityInDb.size());
        assertEquals("Hradec_Kralove", allCityInDb.get(0));
        assertEquals("Prag", allCityInDb.get(1));
    }
}