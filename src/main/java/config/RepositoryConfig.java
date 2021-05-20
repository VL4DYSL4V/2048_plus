package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import util.PropertyUtils;

import java.util.Properties;

@Configuration
public class RepositoryConfig {

    @Bean
    public Properties savedGamesProperties() {
        return PropertyUtils.loadByLocation("repository/saved_games.properties");
    }

    @Bean
    public Properties preferencesProperties() {
        return PropertyUtils.loadByLocation("repository/preferences.properties");
    }

    @Bean
    public Properties repositoryDirectoryProperties() {
        return PropertyUtils.loadByLocation("repository/repository_directory.properties");
    }

    @Bean
    public Properties themeNameToFileNameProperties() {
        return PropertyUtils.loadByLocation("theme_name_to_file_name.properties");
    }

}
