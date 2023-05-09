package org.example.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileConfigReading {


    public static Properties read (String path) {
        String configFilePath = "src/main/resources/" + path + "/settings.properties";
        Properties properties = new Properties();
        try {
            FileInputStream propsInput = new FileInputStream(configFilePath);
            properties.load(propsInput);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
