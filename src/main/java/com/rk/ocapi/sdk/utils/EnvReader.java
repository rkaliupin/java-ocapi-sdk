package com.rk.ocapi.sdk.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Read .env variables file.
 * Return requested env var.
 *
 * To assign what env to use from the console do: java -jar java-ocapi-sdk.jar -DENVIRONMENT=dev
 */
public class EnvReader {
    public static String getProperty(String propKey) {
        // Set the environment or default to 'dev'
        String env = System.getenv().getOrDefault("ENVIRONMENT", "dev");

        // Load properties from the corresponding env file
        Properties properties = loadProperties(env);

        return properties.getProperty(propKey);
    }

    private static Properties loadProperties(String environment) {
        Properties properties = new Properties();
        String envFileName = ".env." + environment;

        try (FileInputStream input = new FileInputStream(envFileName)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
