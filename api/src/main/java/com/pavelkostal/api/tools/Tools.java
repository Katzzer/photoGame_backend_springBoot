package com.pavelkostal.api.tools;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
        byte[] originalImage = getBytesFromMultipartFile(multipartFile);
        savePhoto(originalImage, savedPhotoId, ".jpeg", 1000); // for savePhoto
        savePhoto(originalImage, savedPhotoId, "_mobileVersion.jpeg", 500); // for savePhotoMobileVersion
        savePhoto(originalImage, savedPhotoId, "_thumbnail.jpeg", 10); // for savePhotoThumbnail
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

    private void savePhoto(byte[] originalImage, long savedPhotoId, String fileSuffix, float targetSizeInKB) {
        byte[] resizedIMage = originalImage;
        if (targetSizeInKB < 500) {
            resizedIMage = resizeImageForThumbnail(originalImage);
        }

        if (getImageSizeInKB(originalImage) > targetSizeInKB) {
            resizedIMage = compressImageToGivenSize(originalImage, targetSizeInKB);
        }

        resizedIMage = rotateResizedImageIfOriginalIsPortrait(originalImage, resizedIMage);

        String imageName = savedPhotoId + fileSuffix;
        File targetFile = new File("r:\\" + imageName);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(resizedIMage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] compressImageToGivenSize(byte[] originalImage, float targetSizeInKB) {
        BufferedImage bufferedImage;

        try (InputStream inputStream = new ByteArrayInputStream(originalImage)) {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        float quality = 1.0f;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] finalImageBytes = null;

        try {
            do {
                byteArrayOutputStream.reset();
                float compressedImageSizeInKB = compressImage(bufferedImage, quality, byteArrayOutputStream);
                if (compressedImageSizeInKB <= targetSizeInKB) {
                    finalImageBytes = byteArrayOutputStream.toByteArray();
                    break;
                }
                quality -= 0.1f;
            } while (quality > 0.1f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return finalImageBytes != null ? finalImageBytes : byteArrayOutputStream.toByteArray();
    }

    private float compressImage(BufferedImage image, float quality, ByteArrayOutputStream outputStream) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.next();

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
        }

        return outputStream.size() / 1024.0f;
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

    public byte[] rotateResizedImageIfOriginalIsPortrait(byte[] originalImageBytes, byte[] resizedImageBytes) {
        try (InputStream inputStream = new ByteArrayInputStream(originalImageBytes)) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

                // 6 and 8 correspond to portrait mode for various camera orientations
                if (orientation == 6 || orientation == 8) {
                    BufferedImage image;

                    try (InputStream inputStreamRezied = new ByteArrayInputStream(resizedImageBytes)) {
                        image = ImageIO.read(inputStreamRezied);
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading resized image: " + e.getMessage(), e);
                    }

                    BufferedImage rotatedImage = Scalr.rotate(image, Scalr.Rotation.CW_270);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    ImageIO.write(rotatedImage, "jpeg", outputStream);

                    return outputStream.toByteArray();
                }
            }

            return resizedImageBytes;
        } catch (Exception e) {
            throw new RuntimeException("Error reading image metadata: " + e.getMessage(), e);
        }
    }

    private double getImageSizeInKB(byte[] imageBytes) {
        return imageBytes.length / 1024.0;
    }

    private byte[] resizeImageForThumbnail(byte[] originalImage) {
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

        int fixedImageWidth = 200;

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
}
