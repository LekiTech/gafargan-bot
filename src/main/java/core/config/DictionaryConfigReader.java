package core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DictionaryConfigReader {

    private final Map<String, String> fileMap = new HashMap<>();

    public DictionaryConfigReader() {
        readConfigValues();
    }

    private void readConfigValues() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            for (String key : properties.stringPropertyNames()) {
                // Добавить значения из config.properties в мапу
                fileMap.put(key, properties.getProperty(key));
            }

        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + e.getMessage());
        }
    }

    public String getFilePath(String key) {
        return fileMap.get(key);
    }
}
