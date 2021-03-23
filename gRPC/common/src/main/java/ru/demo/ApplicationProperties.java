package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class ApplicationProperties {
    private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";
    private static final int DEFAULT_PORT = 50051;
    private static final String DEFAULT_HOST = "localhost";

    private static final String SERVER_HOST = "server.host";
    private static final String SERVER_PORT = "server.port";

    public static Optional<Properties> readPropertiesFromFile() {
        return readPropertiesFromFile(DEFAULT_PROPERTIES_FILE);
    }

    public static Optional<Properties> readPropertiesFromFile(String propertiesFileName) {
        try (var inputPropertiesStream = ApplicationProperties.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputPropertiesStream == null) {
                log.error("Can't open properties file:{}", propertiesFileName);
                return Optional.empty();
            }

            var properties = new Properties();
            properties.load(inputPropertiesStream);
            return Optional.of(properties);

        } catch (IOException ex) {
            log.error("Can't open properties file:{}", propertiesFileName, ex);
            return Optional.empty();
        }
    }

    public static int getServerPort() {
        return getServerPort(DEFAULT_PROPERTIES_FILE);
    }

    public static int getServerPort(String propertiesFileName) {
        var properties = readPropertiesFromFile(propertiesFileName)
                .orElseThrow(() -> new RuntimeException("Application properties were not loaded, propertiesFileName:" + propertiesFileName));

        String port = (String) properties.getOrDefault(SERVER_PORT, DEFAULT_PORT);
        return Integer.parseInt(port);
    }

    public static String getServerHost() {
        return getServerHost(DEFAULT_PROPERTIES_FILE);
    }

    public static String getServerHost(String propertiesFileName) {
        var properties = readPropertiesFromFile(propertiesFileName)
                .orElseThrow(() -> new RuntimeException("Application properties were not loaded, propertiesFileName:" + propertiesFileName));

        return (String) properties.getOrDefault(SERVER_HOST, DEFAULT_HOST);
    }

    private ApplicationProperties() {
    }
}
