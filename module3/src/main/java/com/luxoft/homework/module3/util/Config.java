package com.luxoft.homework.module3.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.task1.properties";

    private String fileName;
    private Properties config = null;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public Properties getConfig() {
        if (config != null) {
            return config;
        }

        config = new Properties();
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            config.load(inputStream);
        } catch (Exception ignored) {
        }

        return config;
    }
}
