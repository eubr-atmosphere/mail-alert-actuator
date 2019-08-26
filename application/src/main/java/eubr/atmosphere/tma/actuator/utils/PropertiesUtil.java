package  eubr.atmosphere.tma.actuator.utils;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties readProperties(String fileName) throws Exception {
        Properties properties = new Properties();
        Properties mProperties = loadProperties(fileName);
        properties.putAll(mProperties);

        return properties;
    }

    private static Properties loadProperties(String fileName) throws Exception {
        Properties prop = new Properties();
        InputStream fileInputStream = PropertiesUtil.class.getResourceAsStream(fileName);
        
        try {
            prop.load(fileInputStream);
        } catch (FileNotFoundException var12) {
            throw new Exception(String.format("Property file %s not found.", fileName), var12);
        } catch (IOException var13) {
            throw new Exception(var13.getMessage(), var13);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return prop;
    }
}
