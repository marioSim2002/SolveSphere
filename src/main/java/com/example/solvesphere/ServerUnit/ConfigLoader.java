package com.example.solvesphere.ServerUnit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    /* class to get config properties */
    private static ConfigLoader instance;
    private Properties properties;

    private ConfigLoader() {
        properties = new Properties();
        loadProperties();
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("ServerConfig.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find config.properties");
                return;
            }
            properties.load(input);
            System.out.println("Loaded properties: " + properties);  // Debugging message
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("missing property: " + key);
        }
        return value.trim();
    }

    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw e;
        }
    }
}
