package com.pavelkostal.api.utils.onStart;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.utils.tools.Tools;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnStart {

    private final PhotoRepository photoRepository;
    private final Tools tools;

//    @Value("${save-photo-path}")
//    public String savePhotoPath;

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
    public void saveNewImagesToDatabase() throws IOException {
        // Load images from the resources/images/ folder
        Resource image1 = new ClassPathResource("images/image1.jpg");
        Resource image2 = new ClassPathResource("images/image2.jpg");
        
        // Your logic to save the images to the database
        Photo photo1 = new Photo("2d0a7d01-cb95-4ae2-89bd-93cb7ac7b8ba", 50.20923, 15.83277, "Hradec Kralove", "Czechia", "", "Czech Republic", "");
        Photo photo2 = new Photo("2d0a7d01-cb95-4ae2-89bd-93cb7ac7b8ba", 50.20923, 15.83277, "Hradec Kralove", "Czechia", "", "Czech Republic", "");
        
        Photo savedPhoto1 = photoRepository.save(photo1);
        Photo savedPhoto2 = photoRepository.save(photo2);
        
        tools.savePhotoWithThumbnail(convertInputStreamToMultipartFile(image1.getInputStream(), "image1.jpg"), savedPhoto1.getId());
        tools.savePhotoWithThumbnail(convertInputStreamToMultipartFile(image2.getInputStream(), "image2.jpg"), savedPhoto2.getId());
    }
    
    private MultipartFile convertInputStreamToMultipartFile(InputStream inputStream, String originalFileName) {
        try {
            // Create a new MockMultipartFile instance
			return new MockMultipartFile(originalFileName, originalFileName, "image/jpeg", inputStream);
        } catch (IOException e) {
            log.error("Error occurred during conversion of InputStream to MultipartFile", e);
            return null;
        }
    }

//    @Bean
//    @Profile("dev")
//    public void resizeAllAlreadySavedFiles() {
//        List<Path> fileList = new ArrayList<>();
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(savePhotoPath))) {
//            for (Path path : stream) {
//                if (!Files.isDirectory(path)) {
//                    fileList.add(path);
//                }
//            }
//        } catch(IOException e) {
//            throw new RuntimeException("Error while accessing directory", e);
//        }
//
//        List<Path> listOfMainPhotos = fileList.stream()
//                .filter(file -> {
//                    String fileName = file.getFileName().toString();
//                    return fileName.matches("\\d+\\.jpeg");
//                })
//                .toList();
//
//        for (Path mainPhoto : listOfMainPhotos) {
//            String photoNumber = mainPhoto.getFileName().toString().replace(".jpeg", "");
//            Optional<Photo> photoOptional = photoRepository.findById(Long.valueOf(photoNumber));
//            if (photoOptional.isPresent()) {
//                Photo photoFromDb = photoOptional.get();
//                tools.savePhotoWithThumbnail(convertFileToMultipartFile(mainPhoto.toFile()), photoFromDb.getId());
//            }
//        }
//    }

    private void deleteAllFilesInOnTempDirectory() {
        File dir = new File("temp");
        try {
            if (dir.exists()) {
                FileUtils.cleanDirectory(dir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    private MultipartFile convertFileToMultipartFile(File file)  {
//        FileInputStream input;
//        try {
//            input = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            log.error("File not found !!!!!");
//            throw new RuntimeException(e);
//        }
//
//        MultipartFile multipartFile;
//        try {
//            multipartFile = new MockMultipartFile(
//                    "file",
//                    file.getName(),
//                    "image/jpeg",
//                    IOUtils.toByteArray(input));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return multipartFile;
//    }
}
