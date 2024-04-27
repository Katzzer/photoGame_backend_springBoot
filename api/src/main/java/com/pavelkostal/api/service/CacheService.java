package com.pavelkostal.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
public class CacheService {
	
	@Value("${save-photo-path}")
	private String savePhotoPath;
	
	@Cacheable(value = "photoGame:photos", key = "#p0") // there is problem with key = "#imageName" so is used #p0 that means key is first parameter
	public byte[] getImage(String imageName) {
		log.info("Getting image {} from data storage", imageName);
		
		byte[] imageAsBytes;
		File file = new File(savePhotoPath + File.separator + imageName);
		if (!file.exists() || !file.isFile()) {
			// Handle file not found situation
			throw new IllegalArgumentException("File " + imageName + " does not exist.");
		}
		try {
			imageAsBytes = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return imageAsBytes;
	}
}
