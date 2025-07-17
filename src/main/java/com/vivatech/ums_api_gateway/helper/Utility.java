package com.vivatech.ums_api_gateway.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
public class Utility {

    public static String convertEnumToString(String enumString){
        return StringUtils.replace(enumString, "_", " ");
    }

    public static String imageToBase64(String path) {
        try {
            Path filePath = Paths.get("/home/core/ums/static" + "/images/" + path);
            log.info("URI Path : {}", filePath.toAbsolutePath());
            File file = new File(filePath.toUri());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
