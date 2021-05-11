package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile("classpath:" + location).toPath(),
                StandardCharsets.UTF_8)) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
