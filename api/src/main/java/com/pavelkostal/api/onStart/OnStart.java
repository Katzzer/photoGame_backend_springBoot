package com.pavelkostal.api.onStart;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.tools.Tools;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnStart {

    private final PhotoRepository photoRepository;
    private final Tools tools;

    public static final String DELETE_ALL_FILES_IN_TEMP_DIRECTORY_WHEN_APP_STARTS = "deleteAllFilesInOnTempDirectoryWhenAppStarts";

    @PreDestroy
    public void cleanUp() {
        deleteAllFilesInOnTempDirectory();
    }

    @Bean(name = DELETE_ALL_FILES_IN_TEMP_DIRECTORY_WHEN_APP_STARTS)
    @Profile("h2")
    public void deleteAllFilesInOnTempDirectoryWhenAppStarts() {
       deleteAllFilesInOnTempDirectory(); // if app didn't shut down correctly
    }

    @Bean
    @DependsOn(DELETE_ALL_FILES_IN_TEMP_DIRECTORY_WHEN_APP_STARTS)
    @Profile("h2")
    public void saveNewImagesToDatabase() {
        File image1 = new File("images/image1.jpg");
        File image2 = new File("images/image2.jpg");
        Photo photo1 = new Photo("2d0a7d01-cb95-4ae2-89bd-93cb7ac7b8ba", 50.20923, 15.83277, "Hradec Kralove", "Czechia", "", "Czech Republic", "");
        Photo photo2 = new Photo("2d0a7d01-cb95-4ae2-89bd-93cb7ac7b8ba", 50.20923, 15.83277, "Hradec Kralove", "Czechia", "", "Czech Republic", "");

        Photo savedPhoto1 = photoRepository.save(photo1);
        Photo savedPhoto2 = photoRepository.save(photo2);

        tools.savePhotoWithThumbnail(convertFileToMultipartFile(image1), savedPhoto1.getId());
        tools.savePhotoWithThumbnail(convertFileToMultipartFile(image2), savedPhoto2.getId());
    }

    private void deleteAllFilesInOnTempDirectory() {
        File dir = new File("temp");
        try {
            FileUtils.cleanDirectory(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MultipartFile convertFileToMultipartFile(File file)  {
        FileInputStream input;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("File not found !!!!!");
            throw new RuntimeException(e);
        }

        MultipartFile multipartFile;
        try {
            multipartFile = new MockMultipartFile(
                    "file",
                    file.getName(),
                    "image/jpeg",
                    IOUtils.toByteArray(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return multipartFile;
    }
}
