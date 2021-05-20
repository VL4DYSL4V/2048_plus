package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils(){}

    public static Properties loadByLocation(String location) {
        Objects.requireNonNull(location);
        Properties properties = new Properties();
        try (InputStream inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(location)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
