package dao.theme;

import exception.FetchException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import view.theme.Theme;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Repository("themeDao")
public final class FileSystemThemeDao implements ThemeDao {

    private static final Pattern DIGIT = Pattern.compile("\\d+");
    private final Properties themeNameToFileNameProperties;

    public FileSystemThemeDao(Properties themeNameToFileNameProperties) {
        this.themeNameToFileNameProperties = themeNameToFileNameProperties;
    }

    @Override
    public Theme loadTheme(String path) throws FetchException {
        Properties properties = new Properties();
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                ResourceUtils.getFile(path).toPath())) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new FetchException(e);
        }
        return decodeTheme(properties);
    }

    @Override
    public Theme loadByName(String name) throws FetchException {
        return loadTheme("classpath:" + themeNameToFileNameProperties.getProperty(name));
    }

    private Theme decodeTheme(Properties properties) {
        String name = properties.getProperty("name");
        Color bgColor = parseColor(properties.getProperty("background-color"));
        Color fgColor = parseColor(properties.getProperty("foreground-color"));
        Font font = parseFont(properties);
        Image fieldBgImage = loadFieldBgImage(properties);
        Image welcomeImage = loadWelcomeImage(properties);
        Image gameOverImage = loadGameOverImage(properties);
        Map<Integer, Image> powToImageMap = loadPowerToImageMap(properties);
        return new Theme(name, bgColor, fgColor, font, fieldBgImage, welcomeImage, powToImageMap, gameOverImage);
    }

    private Image loadGameOverImage(Properties properties) {
        Path gameOverImagePath = getPathFromResourceLocation("/" + properties.getProperty("game_over_image"));
        return load(gameOverImagePath.toString());
    }

    private Image loadWelcomeImage(Properties properties) {
        Path welcomeImagePath = getPathFromResourceLocation("/" + properties.getProperty("welcome_image"));
        return load(welcomeImagePath.toString());
    }

    private Image loadFieldBgImage(Properties properties) {
        Path fieldBgPath = getPathFromResourceLocation("/" + properties.getProperty("field_bg_img_path"));
        return load(fieldBgPath.toString());
    }

    private Map<Integer, Image> loadPowerToImageMap(Properties properties) {
        Map<Integer, Image> powToImageMap = new HashMap<>();
        String powerFolderPath = "/" + properties.getProperty("power_folder") + "/";
        for (String key : properties.stringPropertyNames()) {
            if (DIGIT.matcher(key).matches()) {
                String resourceLocation = powerFolderPath + properties.getProperty(key);
                Path path = getPathFromResourceLocation(resourceLocation);
                powToImageMap.put(Integer.valueOf(key), load(path.toString()));
            }
        }
        return powToImageMap;
    }

    private Path getPathFromResourceLocation(String resourceLocation) {
        URL url = getClass().getResource(resourceLocation);
        try {
            if (url == null) {
                throw new RuntimeException();
            }
            return new File(url.toURI()).toPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Font parseFont(Properties properties) {
        String fontName = properties.getProperty("font-name");
        String fontStyle = properties.getProperty("font-style");
        String fontSize = properties.getProperty("font-size");
        return new Font(fontName, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
    }

    private Color parseColor(String s) {
        String[] rgb = s.split("-");
        return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
    }

    private Image load(String fileName) {
        return Toolkit.getDefaultToolkit().getImage(fileName);
    }
}
