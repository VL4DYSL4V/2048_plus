package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class RepositoryConfig {

    @Bean
    public Properties savedGamesProperties() {
        return loadByLocation("repository/saved_games.properties");
    }

    @Bean
    public Properties preferencesProperties() {
        return loadByLocation("repository/preferences.properties");
    }

    @Bean
    public Properties repositoryDirectoryProperties() {
        return loadByLocation("repository/repository_directory.properties");
    }

    @Bean
    public Properties themeNameToFileNameProperties() {
        return loadByLocation("theme_name_to_file_name.properties");
    }

    private Properties loadByLocation(String location) {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(location)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
