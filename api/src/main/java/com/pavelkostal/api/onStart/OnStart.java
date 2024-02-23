package com.pavelkostal.api.onStart;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class OnStart {

    @Bean("h2")
    public void deleteAllFilesInTempDirectory() {
        File dir = new File("temp");
        try {
            FileUtils.cleanDirectory(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
