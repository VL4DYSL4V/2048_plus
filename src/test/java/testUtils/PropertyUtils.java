package testUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils(){}

    private static Properties loadByLocation(String location) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(location)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static Properties getRepositoryDirectoryProperties(){
        return PropertyUtils.loadByLocation("repository/repository_directory.properties");
    }

    public static Properties getTestSavedGamesProperties(){
        Properties out = new Properties();
        out.setProperty("saved-games-directory-name", "savedGamesTest");
        out.setProperty("file.three-and-three", "three_and_three.txt");
        out.setProperty("file.four-and-four", "four-and-four.txt");
        out.setProperty("file.five-and-five", "five_and_five.txt");
        out.setProperty("file.six-and-six", "six_and_six.txt");
        return out;
    }

}
