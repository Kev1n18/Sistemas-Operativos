package com.example.config;

import com.example.util.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private static Configuration currentConfiguration;

    private ConfigurationManager() { }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void loadConfigurationFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonNode node = Json.parse(reader);
            currentConfiguration = Json.fromJson(node, Configuration.class);
        } catch (IOException e) {
            throw new HttpConfigurationException("Failed to load configuration file", e);
        }
    }

    public Configuration getCurrentConfiguration() {
        if (currentConfiguration == null) {
            throw new HttpConfigurationException("No current configuration set.");
        }
        return currentConfiguration;
    }
}
