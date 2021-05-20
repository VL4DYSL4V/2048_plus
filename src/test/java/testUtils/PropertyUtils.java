package testUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils(){}

    public static Properties loadByLocation(String location) {
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

    public static Properties getThemeNameToFileProperties(){
        return PropertyUtils.loadByLocation("theme_name_to_file_name.properties");
    }

    public static Properties getTestPreferencesProperties(){
        Properties out = new Properties();
        out.setProperty("preferences-directory-name", "preferencesTest");
        out.setProperty("preferences-file-name", "preferences.txt");
        return out;
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
