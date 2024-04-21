import java.io.*;
import java.util.Properties;

// This class manages the configuration of the application
// Mainly its purpose is to store and retrieve the file path where the events are stored
// The file path is stored in a properties file named config.properties
// The properties file is loaded when the application starts
// If the config.properties -file does not exist, this class will create it
// If the creation fails, EventManager will use a default file path

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                    properties.load(fis);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle error
        }
    }

    public static void setFilePath(String filePath) {
        properties.setProperty("EVENTS_FILE_PATH", filePath);
        saveConfig();
    }

    public static String getFilePath() {
        return properties.getProperty("EVENTS_FILE_PATH");
    }

    private static void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace(); // Handle error
        }
    }
}
