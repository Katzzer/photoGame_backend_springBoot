package com.pavelkostal.api.tools;

import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class Tools {

    public List<String> replaceUnderscoreWithSpaceForString(List<String> listOfCity) {
        List<String> newList = new ArrayList<>();
        for (String city : listOfCity) {
            newList.add(city.replace("_", " "));
        }

        return newList;
    }
    
    public String replaceSpecialCharactersInString(String text) {
        text = text.replace("ě", "e");
        text = text.replace("š", "s");
        text = text.replace("č", "c");
        text = text.replace("ř", "r");
        text = text.replace("ž", "z");
        text = text.replace("ý", "y");
        text = text.replace("á", "a");
        text = text.replace("í", "i");
        text = text.replace("é", "e");
        text = text.replace("ů", "u");
        text = text.replace("ú", "u");

        text = text.replace("Ě", "E");
        text = text.replace("Š", "S");
        text = text.replace("Č", "C");
        text = text.replace("Ř", "R");
        text = text.replace("Ž", "Z");
        text = text.replace("Ý", "Y");
        text = text.replace("Á", "A");
        text = text.replace("Í", "I");
        text = text.replace("É", "E");
        text = text.replace("Ú", "U");
        
        return text;
    }

    public void savePhotoWithThumbnail(MultipartFile multipartFile, long savedPhotoId) {
        byte[] bufferedImage = getBytesFromMultipartFile(multipartFile);
        savePhoto(bufferedImage, savedPhotoId);
        savePhotoThumbnail(bufferedImage, savedPhotoId);
    }

    private byte[] getBytesFromMultipartFile(MultipartFile multipartFile) {
        InputStream originalImage;
        try {
            originalImage = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer ;
        try {
            buffer = new byte[originalImage.available()];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            @SuppressWarnings("unused")
            int read = originalImage.read(buffer);// without this line of code photo are not saved correctly, DO NOT REMOVE
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buffer;
    }

    private void savePhoto(byte[] bufferedImage, long savedPhotoId) {
        String imageName = savedPhotoId + ".jpeg";
        File targetFile = new File("r:\\" + imageName);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(bufferedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void savePhotoThumbnail(byte[] bufferedImage, long savedPhotoId) {
        byte[] thumbnail = createThumbnail(bufferedImage);
        String thumbnailImageName = savedPhotoId + "_thumbnail.jpeg";
        File thumbnailtargetFile = new File("r:\\" + thumbnailImageName);

        try (OutputStream outStream = new FileOutputStream(thumbnailtargetFile)) {
            outStream.write(thumbnail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createThumbnail(byte[] originalImage) {
        InputStream helperInputStream = new ByteArrayInputStream(originalImage);
        InputStream inputStream = new ByteArrayInputStream(originalImage);

        int imageWidth;
        int imageHeight;
        try {
            BufferedImage read = ImageIO.read(helperInputStream); // stream is closed after reading : https://docs.oracle.com/javase/7/docs/api/javax/imageio/ImageIO.html#read%28java.io.InputStream%29
            imageWidth = read.getWidth();
            imageHeight = read.getHeight();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int fixedImageWidth = 100;

        double ratio = imageWidth / (double) fixedImageWidth;
        int newImageHeight = (int) (imageHeight / ratio);

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage resizedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, fixedImageWidth, newImageHeight);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, "jpeg", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] toByteArray(BufferedImage bi, String format) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, format, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();

    }
}
