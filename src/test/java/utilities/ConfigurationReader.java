package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties properties = new Properties();

    static {
        try {
            // Locates and opens your properties file
            FileInputStream file = new FileInputStream("src/test/resources/configuration.properties");
            // Loads the key-value pairs into memory
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.out.println("CRITICAL ERROR: Failed to load configuration.properties file.");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
