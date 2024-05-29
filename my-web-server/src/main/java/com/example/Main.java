package com.example;

import com.example.config.Configuration;
import com.example.config.ConfigurationManager;
import com.example.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Server starting...");

        try {

            ConfigurationManager.getInstance().loadConfigurationFile("src/main/java/resources/config/http.json");
            Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();
            String webroot = new File(config.getWebroot()).getAbsolutePath();

            ServerListenerThread serverThread = new ServerListenerThread(config.getPort(), webroot);
            serverThread.start();
        } catch (Exception e) {
            LOGGER.error("General error", e);
        }
    }
}
