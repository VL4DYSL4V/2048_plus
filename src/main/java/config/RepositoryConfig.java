package config;

import enums.FieldDimension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class RepositoryConfig {

    @Bean
    public Map<FieldDimension, Path> fieldRepositories() {
        Map<FieldDimension, Path> out = new HashMap<>();
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile("classpath:repository/game.properties").toPath(),
                StandardCharsets.UTF_8)) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String key : properties.stringPropertyNames()) {
            out.put(FieldDimension.valueOf(key.toUpperCase()), Paths.get(properties.getProperty(key)));
        }
        return out;
    }

    @Bean
    public Path preferencesRepository() {
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile("classpath:repository/preferences.properties").toPath(),
                StandardCharsets.UTF_8)) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Paths.get(properties.getProperty("preferences"));
    }

    @Bean
    public Properties themeNameToFileNameProperties() {
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile("classpath:theme_name_to_file_name.properties").toPath())) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
