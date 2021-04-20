package dao.theme;

import exception.FetchException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import view.theme.Theme;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@Repository("themeDao")
public final class FileSystemThemeDao implements ThemeDao {

    private static final Pattern DIGIT = Pattern.compile("\\d+");

    @Override
    public Theme loadTheme(String path) throws FetchException {
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile(path).toPath())) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new FetchException(e);
        }
        return encodeIntoTheme(properties);
    }

    @Override
    public Theme loadByName(String name) throws FetchException {
        Properties properties = themeNameToFileNameProperties();
        return loadTheme("classpath:" + properties.getProperty(name));
    }

    @Override
    public Collection<Theme> loadAll() throws FetchException {
        Collection<Theme> out = new ArrayList<>();
        Properties properties = themeNameToFileNameProperties();
        for (Object name: properties.keySet()) {
            Theme t = loadByName((String) name);
            out.add(t);
        }
        return out;
    }

    private Properties themeNameToFileNameProperties() throws FetchException {
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile("theme_name_to_file_name.properties").toPath())) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new FetchException(e);
        }
        return properties;
    }

    private Theme encodeIntoTheme(Properties properties) {
        String name = properties.getProperty("name");
        Color bgColor = new Color(Integer.parseInt(properties.getProperty("bg_red")),
                Integer.parseInt(properties.getProperty("bg_green")), Integer.parseInt(properties.getProperty("bg_blue")));
        Color fgColor = new Color(Integer.parseInt(properties.getProperty("fg_red")),
                Integer.parseInt(properties.getProperty("fg_green")), Integer.parseInt(properties.getProperty("fg_blue")));
        Image fieldBgImage = load(properties.getProperty("field_bg_img_path"));
        Image welcomeImage = load(properties.getProperty("welcome_image"));
        Path powerFolder = Paths.get(properties.getProperty("power_folder"));
        Map<Integer, Image> powToImageMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if(DIGIT.matcher(key).matches()){
                Path path = powerFolder.resolve((String) properties.get(key));
                powToImageMap.put(Integer.valueOf(key), load(path.toString()));
            }
        }
        Image gameOverImage = load(properties.getProperty("game_over_image"));
        return new Theme(name, bgColor, fgColor, fieldBgImage, welcomeImage, powToImageMap, gameOverImage);
    }

    private Image load(String fileName) {
        return Toolkit.getDefaultToolkit().getImage(fileName);
    }
}
