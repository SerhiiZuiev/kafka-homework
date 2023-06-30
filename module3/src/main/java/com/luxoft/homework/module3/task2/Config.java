package com.luxoft.homework.module3.task2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.task2.properties";


    private static Properties config;


    public static Properties getConfig() throws IOException {
        if (config != null) {
            return config;
        }

        config = new Properties();
        try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            config.load(inputStream);
        }

        return config;
    }
}
